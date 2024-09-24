package com.facebook.notification.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import com.facebook.notification.kafka.NotificationConsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ServerEndpoint("/notification")
public class Service {

	private static Map<Integer, Set<Session>> userSessions = new HashMap<>();

	@OnOpen
	public void onOpen(Session session) {
		Integer userId = getUserIdFromSession(session);
		userSessions.computeIfAbsent(userId, k -> new HashSet<>()).add(session);

		if (!NotificationConsumer.userConsumer.containsKey(userId)) {
			new NotificationConsumer(userId).consume(userId);
		}
	}

	@OnClose
	public void onClose(Session session) {
		Integer userId = getUserIdFromSession(session);
		Set<Session> sessions = userSessions.get(userId);
		if (sessions != null) {
			sessions.remove(session);
			if (sessions.isEmpty()) {
				NotificationConsumer consumer = NotificationConsumer.userConsumer.get(userId);
				if (consumer != null) {
					consumer.removeConsumer(userId,consumer);
				}
				userSessions.remove(userId);
			}
		}
	}

	public static void sendNotification(Integer taggedUserId, String message) {
		Set<Session> sessions = userSessions.get(taggedUserId);
		if (sessions != null) {
			for (Session session : sessions) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Integer getUserIdFromSession(Session session) {
		Map<String, List<String>> params = session.getRequestParameterMap();
		return Integer.parseInt(params.get("userId").get(0));
	}
}





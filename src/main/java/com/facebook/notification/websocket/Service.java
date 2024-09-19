package com.facebook.notification.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.facebook.notification.kafka.NotificationConsumer;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/notification")
public class Service {
	
    private static Set<Session> clients = new HashSet<>();
    private static boolean kafkaConsumerStarted = false;
    
    @OnOpen
    public void onOpen(Session session) {
    	clients.add(session);
        if (!kafkaConsumerStarted) {
    		new NotificationConsumer().consume();
            kafkaConsumerStarted = true;
        }
        System.out.println("New connection opened: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message from client: " + message);
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }

    public static void broadcast(String message) {
        for (Session client : clients) {
            try {
                client.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

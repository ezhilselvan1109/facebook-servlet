package com.facebook.api.friend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.facebook.api.response.ApiResponse;
import com.facebook.api.user.User;
import com.facebook.database.service.FriendService;
import com.facebook.database.service.PostService;
import com.facebook.database.service.UserService;
import com.facebook.notification.Notification;
import com.facebook.notification.kafka.NotificationProducer;

import jakarta.servlet.http.HttpServletResponse;

public class Service {
	private static final NotificationProducer notificationProducer = new NotificationProducer();

	public static void request(int from, int to, HttpServletResponse response) throws IOException {
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (FriendService.request(from, to)) {
			List<User> user = UserService.profile(from);
			notificationProducer.sendNotification("friend", to,
					new JSONObject(new Notification(null, user, "accept my friend request", null, null)).toString());
			msg.add("successfully requested");
			statusCode = HttpServletResponse.SC_CREATED;
			message = "Success";
		} else {
			msg.add("request Failed");
			statusCode = HttpServletResponse.SC_NOT_FOUND;
			message = "Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}

	public static void accept(int from, int to, HttpServletResponse response) throws IOException {
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (FriendService.accept(from, to)) {
			List<User> user = UserService.profile(from);
			notificationProducer.sendNotification("friend", to,
					new JSONObject(new Notification(null, user, "accepted your friend request", null, null)).toString());
			
			msg.add("successfully accepted");
			statusCode = HttpServletResponse.SC_CREATED;
			message = "Success";
		} else {
			msg.add("accept Failed");
			statusCode = HttpServletResponse.SC_NOT_FOUND;
			message = "Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());

	}

	public static void reject(int from, int to, HttpServletResponse response) throws IOException {
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (FriendService.reject(from, to)) {
			msg.add("successfully rejected");
			statusCode = HttpServletResponse.SC_NO_CONTENT;
			message = "Success";
		} else {
			msg.add("reject Failed");
			statusCode = HttpServletResponse.SC_NOT_FOUND;
			message = "Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());

	}

	public static void list(int user_id, HttpServletResponse response) throws IOException {
		List<User> user = FriendService.list(user_id);
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (user.isEmpty()) {
			msg.add("User Not Found");
			statusCode = 404;
			message = "Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg.isEmpty() ? user : msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());

	}
}

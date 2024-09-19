package com.facebook.api.comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.facebook.api.response.ApiResponse;
import com.facebook.api.user.User;
import com.facebook.database.service.CommentService;
import com.facebook.database.service.PostService;
import com.facebook.database.service.UserService;
import com.facebook.notification.Notification;
import com.facebook.notification.kafka.NotificationProducer;

import jakarta.servlet.http.HttpServletResponse;

public class Service {
	private static final NotificationProducer notificationProducer = new NotificationProducer();
	
	public static void create(int post_id,int user_id,String comment,List<Integer> taggedId,HttpServletResponse response) throws IOException {
		List<String> msg=new ArrayList<>();
		int statusCode=0;
		String message="";
		if(CommentService.create(post_id,user_id,comment,taggedId)) {
			msg.add("comment successful");
			statusCode=HttpServletResponse.SC_CREATED;
			message="Success";
			List<User> user=UserService.profile(user_id);
			int post_user_id=PostService.getUserId(post_id);
    		JSONObject jsonResponse = new JSONObject(new Notification(post_user_id,post_id,user,comment,taggedId));
	        notificationProducer.sendNotification(jsonResponse.toString());
		}else {
			msg.add("comment unsuccessful");
			statusCode=HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			message="Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}
	
	public static void list(int post_id,HttpServletResponse response) throws IOException {
		List<Comment> posts = CommentService.list(post_id);
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (posts.isEmpty()) {
			msg.add("Data is not found");
			statusCode = HttpServletResponse.SC_NOT_FOUND;
			message = "Failed";
		} 
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg.isEmpty()?posts:msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}
}

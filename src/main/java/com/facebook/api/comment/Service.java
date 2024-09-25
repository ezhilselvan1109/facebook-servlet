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
			List<User> tagged=null;
			if(taggedId.size()>0) {
				tagged=UserService.profile(taggedId);
			}
	        if(taggedId.contains(post_user_id)) {
	        	notificationProducer.sendNotification("tag",post_user_id, new JSONObject(new Notification(post_id,user,"you were tagged "+(taggedId.size()>1?" and others ":"")+"on Your post "+(!comment.isEmpty()?" and comment : "+comment:""),tagged)).toString());
	        }else if(taggedId.size()>0){
	        	notificationProducer.sendNotification("tag",post_user_id, new JSONObject(new Notification(post_id,user,"Your post was tagged"+(!comment.isEmpty()?" and comment : "+comment:""),tagged)).toString());
	        }else if(post_user_id!=user_id){
	        	notificationProducer.sendNotification("comment",post_user_id, new JSONObject(new Notification(post_id,user,"Your post was commented : "+comment,tagged)).toString());
	        }
	        
	        for (Integer taggedUser : taggedId) {
	        	if(taggedUser==post_user_id)continue;
	        	notificationProducer.sendNotification("tag",taggedUser, new JSONObject(new Notification(post_id,user,"You were tagged"+(taggedId.size()>1?" and others ":"")+(!comment.isEmpty()?" and comment : "+comment:""),tagged)).toString());
	        }
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

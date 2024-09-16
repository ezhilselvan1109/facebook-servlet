package com.facebook.api.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.facebook.api.response.ApiResponse;
import com.facebook.api.user.User;
import com.facebook.database.service.PostService;

import jakarta.servlet.http.HttpServletResponse;

public class Service {

	public static void createPost(int user_id, String description, byte[] profileImage,HttpServletResponse response) throws IOException {
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (PostService.createPost(user_id, description, profileImage)) {
			msg.add("Post is Created");
			statusCode = HttpServletResponse.SC_CREATED;
			message = "Success";
		} else {
			msg.add("A database error occurred");
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			message = "Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
		
	}

	public static void like(int user_id, int post_id,HttpServletResponse response) throws IOException {
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (PostService.like(user_id, post_id)) {
			msg.add("successfully liked");
			statusCode = HttpServletResponse.SC_CREATED;
			message = "Success";
		} else {
			msg.add("Liked Failed");
			statusCode = HttpServletResponse.SC_CONFLICT;
			message = "Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}

	public static void unlike(int user_id, int post_id,HttpServletResponse response) throws IOException {
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (PostService.unlike(user_id, post_id)) {
			msg.add("successfully unliked");
			statusCode = HttpServletResponse.SC_NO_CONTENT;
			message = "Success";
		} else {
			msg.add("unliked Failed");
			statusCode = HttpServletResponse.SC_NOT_FOUND;
			message = "Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}

	public static void all(int id,HttpServletResponse response) throws IOException {
		List<Post> posts = PostService.getPosts(id);
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (posts.isEmpty()) {
			msg.add("Data is not found");
			statusCode = HttpServletResponse.SC_NOT_FOUND;
			message = "Failed";
		} 
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg.isEmpty()?posts: msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}

	public static void user(int id,int from,HttpServletResponse response) throws IOException {
		List<User> posts = PostService.getUserPosts(id,from);
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (posts.isEmpty()) {
			msg.add("Data is not found");
			statusCode = HttpServletResponse.SC_NOT_FOUND;
			message = "Failed";
		} 
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message,msg.isEmpty()?posts: msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}
}

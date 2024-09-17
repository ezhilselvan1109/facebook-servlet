package com.facebook.api.user;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.facebook.api.response.ApiResponse;
import com.facebook.database.service.PostService;
import com.facebook.database.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

public class Service {
	public static void accountCreate(String firstName, String lastName, Date dateOfBirth, String password,
			String email, int phone,HttpServletResponse response) throws IOException {
		List<String> msg = new ArrayList<>();
		boolean isExists = UserService.isUserExists(email, phone);
		int statusCode = 200;
		String message = "Success";
		if (isExists) {
			msg.add("Account is already Exits");
			statusCode = HttpServletResponse.SC_CONFLICT;
			message = "Failed";
		} else {
			if (UserService.accountCreate(firstName, lastName, dateOfBirth, password, email, phone)) {
				msg.add("Account is Created");
				statusCode = HttpServletResponse.SC_CREATED;
				message = "Success";
			} else {
				msg.add("A database error occurred");
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
				message = "Failed";
			}
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}
	
	public static void profile(int user_id,byte[] profileImage,HttpServletResponse response) throws IOException {
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if (UserService.profile(user_id,profileImage)) {
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

	public static void search(String key,int id,HttpServletResponse response) throws IOException {
		List<User> users = UserService.getUsersByKey(key,id);
		int statusCode = 200;
		String message = "Success";
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, users);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}

	public static void all(HttpServletResponse response) throws IOException {
		List<User> users = UserService.getUsers();
		int statusCode = 200;
		String message = "Success";
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, users);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}

	public static void user(int id,HttpServletResponse response) throws IOException {
		List<User> user = UserService.profile(id);
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if(user.isEmpty()) {
			msg.add("User Not Found");
			statusCode = 404;
			message = "Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg.isEmpty()?user:msg);
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
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg.isEmpty()?posts:msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}

	public static void profile(int id,HttpServletResponse response) throws IOException {
		List<User> user = UserService.profile(id);
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if(user.isEmpty()) {
			msg.add("User Not Found");
			statusCode = 404;
			message = "Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg.isEmpty()?user:msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}

	public static void profile(int id, int from,HttpServletResponse response) throws IOException {
		List<User> user = UserService.profile(id,from);
		List<String> msg = new ArrayList<>();
		int statusCode = 200;
		String message = "Success";
		if(user.isEmpty()) {
			msg.add("User Not Found");
			statusCode = 404;
			message = "Failed";
			
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg.isEmpty()?user:msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}

}

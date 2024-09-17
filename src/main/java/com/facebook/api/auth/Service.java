package com.facebook.api.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.facebook.api.response.ApiResponse;
import com.facebook.database.service.UserService;
import com.facebook.util.Password;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Service {
	public static void login(String username,String password,HttpServletRequest request,HttpServletResponse response) throws IOException {
		List<String> msg=new ArrayList<>();
		String resultPassword=UserService.getPassword(username);
		int statusCode=0;
		String message="";
        if(resultPassword!="" && Password.checkPassword(password, resultPassword)) {
        	int id=UserService.getId(username);
        	statusCode=HttpServletResponse.SC_OK;
        	msg.add("Access Granded");
        	message="Success";
            HttpSession session = request.getSession();
            session.setAttribute("user_id", id);
            Cookie sessionCookie = new Cookie("SESSIONID", session.getId());
            sessionCookie.setHttpOnly(true);
            sessionCookie.setMaxAge(30 * 60);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
            response.setStatus(HttpServletResponse.SC_OK);
        }else if(resultPassword=="") {
        	statusCode=HttpServletResponse.SC_NOT_FOUND;
        	msg.add("Account does not exist.");
        	message="Failed";
        }else {
        	statusCode=HttpServletResponse.SC_UNAUTHORIZED;
        	msg.add("Password was worng");
        	message="Failed";
        }
        response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}
	
	public static void forgotPassword(String username,HttpServletResponse response) throws IOException {
		List<String> msg=new ArrayList<>();
		Map<String,Integer> result=new HashMap<>();
		int statusCode=200;
		String message="";
		int id=UserService.getUserId(username);
		if(id==-1) {
			msg.add("Account does not exist.");
			statusCode=HttpServletResponse.SC_NOT_FOUND;
			message="Failed";
		}else {
			result.put("id", id);
			statusCode=HttpServletResponse.SC_OK;
			message="Success";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg.isEmpty()?result:msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}
	
	public static void changePassword(int id,String password,HttpServletResponse response) throws IOException {
		List<String> msg=new ArrayList<>();
		int statusCode=0;
		String message="";
		if(UserService.changePassword(id,password)) {
			msg.add("Update successful");
			statusCode=HttpServletResponse.SC_CREATED;
			message="Success";
		}else {
			msg.add("Update unsuccessful");
			statusCode=HttpServletResponse.SC_NOT_FOUND;
			message="Failed";
		}
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(statusCode, message, msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
	}
}

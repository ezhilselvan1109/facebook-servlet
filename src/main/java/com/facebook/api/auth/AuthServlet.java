package com.facebook.api.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.facebook.api.friend.Friend;
import com.facebook.api.response.ApiResponse;
import com.facebook.util.Converter;
import com.facebook.util.Validation;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/login".equals(path)) {
			login(request, response);
		} else if ("/logout".equals(path)) {
			logout(request, response);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/forgotPassword".equals(path)) {
			forgotPassword(request, response);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/changePassword".equals(path)) {
			changePassword(request, response);
		}
	}

	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonData = Converter.json(request);
		String username = jsonData.optString("username");
		String password = jsonData.optString("password");
		List<String> msg = new ArrayList<>();
		if ((username == null || username.equals("")) && (password == null || password.equals(""))) {
			msg.add("Provide Email or Phone number and Password");
		} else if (username == null || username.equals("")) {
			msg.add("Provide Email or Phone number");
		} else if (password == null || password.equals("")) {
			msg.add("Provide password");
		} else if (!Validation.email(username) && !Validation.phoneNumber(username)) {
			msg.add("Provide valid Email or Phone number");
		}
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.login(username, password, request, response);
	}

	public void forgotPassword(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		List<String> msg = new ArrayList<>();
		if (username == null || username.equals(""))
			msg.add("Provide Email or Phone number");
		else if (!Validation.email(username) && !Validation.phoneNumber(username))
			msg.add("Provide valid Email or Phone number");
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.forgotPassword(username, response);
	}

	public void changePassword(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonData = Converter.json(request);
		String id = jsonData.optString("id");
		String password = jsonData.optString("password");
		List<String> msg = new ArrayList<>();
		if (id == null || id.equals(""))
			msg.add("Provide id");

		if (password == null || password.equals(""))
			msg.add("Provide password");

		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.changePassword(Integer.parseInt(id), password, response);
	}

	protected void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		List<String> msg = new ArrayList<>();
		msg.add("Successfully logout");
		Cookie sessionCookie = new Cookie("user_id", "");
		sessionCookie.setMaxAge(0);
		response.addCookie(sessionCookie);
		response.setStatus(HttpServletResponse.SC_OK);
		ApiResponse apiResponse = new ApiResponse(HttpServletResponse.SC_OK, "Success", msg);
		JSONObject jsonResponse = new JSONObject(apiResponse);
		response.getWriter().write(jsonResponse.toString());
		
	}
}
package com.facebook.api.friend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.facebook.api.response.ApiResponse;
import com.facebook.util.Validation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/friend/*")
public class FriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
        if ("/request".equals(path)) {
        	list(request, response);
        }
	}
	
	public void request(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> msg = new ArrayList<>();
		String from=request.getParameter("from");
		String to=request.getParameter("to");
		if (from == null || from.equals(""))
			msg.add("Provide From value");
		else if(!Validation.isInteger(from))
			msg.add("Provide valid from value");
		
		if (to == null || to.equals(""))
			msg.add("Provide to value");
		else if(!Validation.isInteger(to))
			msg.add("Provide valid from value");
		if(from.equals(to))
			msg.add("is not valid request,from and to is same value");
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.request(Integer.parseInt(from), Integer.parseInt(to),response);
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
        if ("/accept".equals(path)) {
        	list(request, response);
        }
	}
	
	public void accept(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String from=request.getParameter("from");
		String to=request.getParameter("to");
		List<String> msg = new ArrayList<>();
		if (from == null || from.equals(""))
			msg.add("Provide From value");
		else if(!Validation.isInteger(from))
			msg.add("Provide valid from value");
		
		if (to == null || to.equals(""))
			msg.add("Provide to value");
		else if(!Validation.isInteger(to))
			msg.add("Provide valid from value");
		if(from.equals(to))
			msg.add("is not valid request,from and to is same value");
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.accept(Integer.parseInt(from), Integer.parseInt(to),response);
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
        if ("/reject".equals(path)) {
        	reject(request, response);
        }
	}
	
	public void reject(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String from=request.getParameter("from");
		String to=request.getParameter("to");
		List<String> msg = new ArrayList<>();
		if (from == null || from.equals(""))
			msg.add("Provide From value");
		else if(!Validation.isInteger(from))
			msg.add("Provide valid from value");
		
		if (to == null || to.equals(""))
			msg.add("Provide to value");
		else if(!Validation.isInteger(to))
			msg.add("Provide valid from value");
		
		if(from.equals(to))
			msg.add("is not valid request,from and to is same value");
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.reject(Integer.parseInt(from), Integer.parseInt(to),response);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
        if ("/list".equals(path)) {
        	list(request, response);
        }
	}
	
	public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user_id=request.getParameter("user_id");
		List<String> msg = new ArrayList<>();
		if (user_id == null || user_id.equals(""))
			msg.add("Provide to value");
		else if(!Validation.isInteger(user_id))
			msg.add("Provide valid from value");
		
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.list(Integer.parseInt(user_id),response);
	}
	
	
}

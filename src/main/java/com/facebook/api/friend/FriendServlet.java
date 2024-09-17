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
import jakarta.servlet.http.HttpSession;

@WebServlet("/api/friend/*")
public class FriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
        if ("/request".equals(path)) {
        	request(request, response);
        }
	}
	
	public void request(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> msg = new ArrayList<>();
		HttpSession session = request.getSession(false);
	    int from=(int) session.getAttribute("user_id");
		String to=request.getParameter("id");
		if (to == null || to.equals(""))
			msg.add("Provide to value");
		else if(!Validation.isInteger(to))
			msg.add("Provide valid from value");
		
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.request(from, Integer.parseInt(to),response);
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
        if ("/accept".equals(path)) {
        	accept(request, response);
        }
	}
	
	public void accept(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
	    int from=(int) session.getAttribute("user_id");
	    String to=request.getParameter("id");
		List<String> msg = new ArrayList<>();
		
		if (to == null || to.equals(""))
			msg.add("Provide to value");
		else if(!Validation.isInteger(to))
			msg.add("Provide valid from value");
		
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.accept(from, Integer.parseInt(to),response);
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
		HttpSession session = request.getSession(false);
	    int from=(int) session.getAttribute("user_id");
	    String to=request.getParameter("id");
		List<String> msg = new ArrayList<>();
		
		if (to == null || to.equals(""))
			msg.add("Provide to value");
		else if(!Validation.isInteger(to))
			msg.add("Provide valid from value");
		
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.reject(from, Integer.parseInt(to),response);
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
			msg.add("Provide id value");
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

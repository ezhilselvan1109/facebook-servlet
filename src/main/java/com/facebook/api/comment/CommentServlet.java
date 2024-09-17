package com.facebook.api.comment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.api.response.ApiResponse;
import com.facebook.util.Converter;

@WebServlet("/api/comment/*")
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/create".equals(path)) {
			create(request, response);
		}
	}
	
	public void create(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonData = Converter.json(request);
		HttpSession session = request.getSession(false);
	    int user_id=(int) session.getAttribute("user_id");
		String post_id = jsonData.optString("post_id");
		String comment = jsonData.optString("comment");
		List<Integer> taggedId = new ArrayList<>();
        if (jsonData.has("taged_id")) {
            JSONArray taged_id = jsonData.getJSONArray("taged_id");
            for (int i = 0; i < taged_id.length(); i++) {
            	taggedId.add(taged_id.getInt(i));
            }
        }
		List<String> msg = new ArrayList<>();
		if (post_id == null || post_id.equals(""))
			msg.add("Provide post_id");

		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.create(Integer.parseInt(post_id), user_id,comment,taggedId,response);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/list".equals(path)) {
			list(request, response);
		}
	}
	
	public void list(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String post_id = request.getParameter("id");
		List<String> msg = new ArrayList<>();
		if (post_id == null || post_id.equals(""))
			msg.add("Provide post_id");

		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.list(Integer.parseInt(post_id),response);
	}
}
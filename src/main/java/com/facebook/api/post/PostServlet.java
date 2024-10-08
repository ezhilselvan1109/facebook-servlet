package com.facebook.api.post;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.facebook.api.response.ApiResponse;
import com.facebook.util.Validation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
@WebServlet("/api/post/*")
public class PostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/create".equals(path)) {
			createPost(request, response);
		} else if ("/like".equals(path)) {
			like(request, response);
		}
	}

	protected void createPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("application/json");
	    response.setStatus(HttpServletResponse.SC_OK);
	    List<String> msg = new ArrayList<>();
	    HttpSession session = request.getSession(false);
	    int userId = (int) session.getAttribute("user_id");
	    String description = request.getParameter("description");
	    Part imagePart = request.getPart("image");
	    if (imagePart == null && (description == null || description.trim().isEmpty())) {
	        msg.add("Provide Image or Description");
	    }
	    byte[] imageData = null;
	    if (imagePart != null && imagePart.getSize() > 0) {
	        if (!Validation.image(imagePart)) {
	            msg.add("Invalid image file type. Allowed types are: JPEG, PNG, GIF.");
	        } else {
	            try (InputStream imageStream = imagePart.getInputStream()) {
	                imageData = imageStream.readAllBytes();
	            } catch (IOException e) {
	                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                ApiResponse apiResponse = new ApiResponse(500, "Failed to process image", e.getMessage());
	                JSONObject jsonResponse = new JSONObject(apiResponse);
	                response.getWriter().write(jsonResponse.toString());
	                return;
	            }
	        }
	    }
	    if (!msg.isEmpty()) {
	        response.setStatus(422);
	        ApiResponse apiResponse = new ApiResponse(422, "One or more validation errors occurred", msg);
	        JSONObject jsonResponse = new JSONObject(apiResponse);
	        response.getWriter().write(jsonResponse.toString());
	        return;
	    }
	    Service.createPost(userId, description, imageData, response);
	}


	public void like(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int user_id = (int) session.getAttribute("user_id");
		String post_id = request.getParameter("post_id");
		List<String> msg = new ArrayList<>();
		if (post_id == null || post_id.equals(""))
			msg.add("Provide Post Id");
		else if (!Validation.isInteger(post_id))
			msg.add("Provide Valid Post Id");

		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.like(user_id, Integer.parseInt(post_id), response);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/unlike".equals(path)) {
			unlike(request, response);
		}
	}

	public void unlike(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int user_id = (int) session.getAttribute("user_id");
		String post_id = request.getParameter("post_id");
		List<String> msg = new ArrayList<>();
		if (post_id == null || post_id.equals(""))
			msg.add("Provide Post Id");
		else if (!Validation.isInteger(post_id))
			msg.add("Provide Valid Post Id");
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.unlike(user_id, Integer.parseInt(post_id), response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/all".equals(path)) {
			all(request, response);
		} else if ("/user".equals(path)) {
			user(request, response);
		}
	}

	public void all(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int user_id = (int) session.getAttribute("user_id");
		Service.all(user_id, response);
	}

	public void user(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user_id = request.getParameter("user_id");
		HttpSession session = request.getSession(false);
		int from = (int) session.getAttribute("user_id");
		List<String> msg = new ArrayList<>();
		if (user_id == null || user_id.equals(""))
			msg.add("Provide User Id");
		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.user(Validation.isInteger(user_id) ? Integer.parseInt(user_id) : 0, from, response);
	}

}
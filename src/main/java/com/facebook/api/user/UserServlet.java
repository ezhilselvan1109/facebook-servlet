package com.facebook.api.user;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
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
@WebServlet("/api/user/*")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/signup".equals(path)) {
			accountCreate(request, response);
		} else if ("/image".equals(path)) {
			image(request, response);
		}
	}

	public void accountCreate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		String dateOfBirth = request.getParameter("dateOfBirth");

		List<String> msg = new ArrayList<>();
		if (firstName == null || firstName.equals(""))
			msg.add("Provide first name");

		if (lastName == null || firstName.equals(""))
			msg.add("Provide last name");

		if (email == null || email.equals(""))
			msg.add("Provide email");
		else if (!Validation.email(email))
			msg.add("Provide valid phone number");

		if (phone == null || phone.equals("")) {
			msg.add("Provide phone number");
		} else if (phone != null && !Validation.phoneNumber(phone)) {
			msg.add("Provide valid phone number");
		}

		if (password == null || password.equals(""))
			msg.add("provide password");

		if (dateOfBirth == null || dateOfBirth.equals(""))
			msg.add("provide dateOfBirth");

		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation errors occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		if (phone == null)
			phone = "";
		Service.accountCreate(firstName, lastName, Date.valueOf(dateOfBirth), password, email, Integer.parseInt(phone),
				response);
	}

	public void image(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<String> msg = new ArrayList<>();
		HttpSession session = request.getSession(false);
		int userId = (int) session.getAttribute("user_id");
		Part imagePart = request.getPart("image");
		if (imagePart == null) {
			msg.add("Provide Image");
		}
		byte[] imageData = null;
		if (imagePart != null) {
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
		Service.profile(userId, imageData, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String path = request.getPathInfo();
		if ("/detail".equals(path)) {
			user(request, response);
		} else if ("/profile".equals(path)) {
			profile(request, response);
		} else if ("/search".equals(path)) {
			search(request, response);
		} else if ("/all".equals(path)) {
			all(request, response);
		}
	}

	public void user(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		int userId = (int) session.getAttribute("user_id");
		Service.user(userId, response);
	}

	public void profile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String from = request.getParameter("from");
		String id = request.getParameter("id");
		List<String> msg = new ArrayList<>();
		if (id == null || id.equals(""))
			msg.add("Provide Id");

		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation errors occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		if (from != null && !from.equals("") && Boolean.valueOf(from) == false) {
			HttpSession session = request.getSession(false);
			int userId = (int) session.getAttribute("user_id");
			Service.profile(Integer.parseInt(id), userId, response);
			return;
		}
		Service.profile(Integer.parseInt(id), response);
	}

	public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		String key = request.getParameter("key");
		HttpSession session = request.getSession(false);
		int id = (int) session.getAttribute("user_id");
		List<String> msg = new ArrayList<>();
		if (key == null || key.equals(""))
			msg.add("Provide key");

		if (!msg.isEmpty()) {
			response.setStatus(422);
			ApiResponse apiResponse = new ApiResponse(422, "One or more validation errors occurred", msg);
			JSONObject jsonResponse = new JSONObject(apiResponse);
			response.getWriter().write(jsonResponse.toString());
			return;
		}
		Service.search(key, id, response);
	}

	public void all(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Service.all(response);
	}
}
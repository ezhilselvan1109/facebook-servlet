package com.facebook.util;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONObject;

import jakarta.servlet.http.HttpServletRequest;

public class Converter {
	public static JSONObject json(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
	    String line;
	    try (BufferedReader reader = request.getReader()) {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	    }
	    String requestBody = sb.toString();
	    return new JSONObject(requestBody);
	}
}

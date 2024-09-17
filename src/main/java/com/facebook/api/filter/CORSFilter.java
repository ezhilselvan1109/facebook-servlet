package com.facebook.api.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.facebook.api.response.ApiResponse;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CORSFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        httpResponse.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        httpResponse.setHeader("Access-Control-Max-Age", "1209600");

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String uri = httpRequest.getRequestURI();
        if (uri.endsWith("/login") || uri.endsWith("/signup") || uri.endsWith("/forgotPassword") || uri.endsWith("/changePassword")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
        	List<String> msg = new ArrayList<>();
    		msg.add("Unauthorized: Please log in");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiResponse apiResponse = new ApiResponse(HttpServletResponse.SC_UNAUTHORIZED, "Failed", msg);
    		JSONObject jsonResponse = new JSONObject(apiResponse);
    		httpResponse.getWriter().write(jsonResponse.toString());
            return;
        }

        chain.doFilter(request, response);
    }

    public void destroy() {}
}


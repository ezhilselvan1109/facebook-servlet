package com.facebook.api.response;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiResponse {
    private int statusCode;
    private String message;
    private Object data;

    public ApiResponse(int statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return statusCode;
    }

    public void setStatus(int status) {
        this.statusCode = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}

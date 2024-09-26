package com.facebook.notification;

import java.io.IOException;
import java.util.List;

import com.facebook.api.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {
	
	@JsonProperty("post_id")
	private Integer post_id;

	@JsonProperty("user")
	private List<User> user;
	

	@JsonProperty("message")
	private String message;
	
	@JsonProperty("comment")
	private String comment;
	
	@JsonProperty("tag")
	private List<User> tag;
	
	public Notification(Integer post_id, List<User> user,String message, String comment,List<User> tag) {
		super();
		this.post_id = post_id;
		this.user = user;
		this.message = message;
		this.comment = comment;
		this.tag=tag;
	}

	public Integer getPost_id() {
		return post_id;
	}

	public void setPost_id(Integer post_id) {
		this.post_id = post_id;
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<User> getTag() {
		return tag;
	}

	public void setTag(List<User> tag) {
		this.tag = tag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}

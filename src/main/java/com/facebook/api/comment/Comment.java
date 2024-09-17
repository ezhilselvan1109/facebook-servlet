package com.facebook.api.comment;

import java.util.List;

import com.facebook.api.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment {
	
	@JsonProperty("id")
	private int id;
	
	@JsonProperty("user")
	private User user;
	
	@JsonProperty("comment")
	private String comment;
	
	@JsonProperty("tag")
	private List<User> tag;
	
	
	public Comment(int id, User user, String comment, List<User> tag) {
		super();
		this.id = id;
		this.user = user;
		this.comment = comment;
		this.tag = tag;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
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
	
	
}

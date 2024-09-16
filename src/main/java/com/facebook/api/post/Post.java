package com.facebook.api.post;

import java.util.Base64;

import com.facebook.api.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post {
	@JsonProperty("id")
	private Integer id;

	@JsonProperty("description")
	private String description;

	@JsonProperty("image")
	private String image;

	@JsonProperty("user")
	private User user;

	@JsonProperty("like")
	private Like like;

	public Post(Integer id, String description, byte[] image, User user, Like like) {
		super();
		this.id = id;
		this.user = user;
		this.description = description;
		this.image = (image != null) ? Base64.getEncoder().encodeToString(image) : null;
		this.like = like;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Like getLike() {
		return like;
	}

	public void setLike(Like like) {
		this.like = like;
	}
	
}

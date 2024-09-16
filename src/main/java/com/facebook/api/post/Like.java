package com.facebook.api.post;

import com.facebook.api.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Like {
	@JsonProperty("count")
	private int count;

	@JsonProperty("isLiked")
	private Boolean isLiked;

	@JsonProperty("user")
	private User user;

	public Like(Boolean isLiked, User user,int count) {
		super();
		this.isLiked = isLiked;
		this.user = user;
		this.count = count;
	}
	
	public Like() {}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Boolean getIsLiked() {
		return isLiked;
	}

	public void setIsLiked(Boolean isLiked) {
		this.isLiked = isLiked;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}

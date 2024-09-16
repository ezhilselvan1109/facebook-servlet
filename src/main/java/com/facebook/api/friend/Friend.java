package com.facebook.api.friend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Friend {
	
	@JsonProperty("isFriend")
	private Boolean isFriend;
	
	@JsonProperty("status")
	private Boolean status;

	public Friend(Boolean isFriend,Boolean status) {
		this.isFriend = isFriend;
		this.status = status;
	}

	public Boolean getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(Boolean isFriend) {
		this.isFriend = isFriend;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	
}

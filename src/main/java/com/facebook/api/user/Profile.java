package com.facebook.api.user;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Base64;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {
	@JsonProperty("image")
	private String image;

	public Profile(byte[] image) {
		super();
		this.image = (image != null) ? Base64.getEncoder().encodeToString(image) : null;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}

package com.facebook.api.user;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Base64;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {
	@JsonProperty("image")
	private String imageBase64;

	public Profile(byte[] image) {
		super();
		this.imageBase64 = (image != null) ? Base64.getEncoder().encodeToString(image) : null;
	}
}

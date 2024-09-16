package com.facebook.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.util.List;

import com.facebook.api.friend.Friend;
import com.facebook.api.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("first_name")
	private String first_name;

	@JsonProperty("last_name")
	private String last_name;

	@JsonProperty("date_of_birth")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date date_of_birth;

	@JsonProperty("email")
	private String email;

	@JsonProperty("phone")
	private Integer phone;

	@JsonProperty("password")
	private String password;
	
	@JsonProperty("friend")
	private Friend friend;
	
	@JsonProperty("profile")
	private Profile profile;
	
	@JsonProperty("post")
	private List<Post> post;

	public User(int id, String firstName, String lastName, Date dateOfBirth, String email, Integer phone,
			String password, Friend friend,Profile profile,List<Post> posts) {
		super();
		this.id = id;
		this.first_name = firstName;
		this.last_name = lastName;
		this.date_of_birth = dateOfBirth;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.friend = friend;
		this.profile = profile;
		this.post = posts;
	}
	public User() {
		this.id = null;
		this.first_name = null;
		this.last_name = null;
		this.date_of_birth = null;
		this.password = null;
		this.email = null;
		this.phone = null;
		this.friend=null;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public Date getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(Date date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getPhone() {
		return phone;
	}
	public void setPhone(Integer phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Friend getFriend() {
		return friend;
	}
	public void setFriend(Friend friend) {
		this.friend = friend;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public List<Post> getPost() {
		return post;
	}
	public void setPost(List<Post> post) {
		this.post = post;
	}
	
	
}

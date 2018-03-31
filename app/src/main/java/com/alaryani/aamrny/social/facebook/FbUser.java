package com.alaryani.aamrny.social.facebook;


import com.alaryani.aamrny.object.User;

public class FbUser {
	String id, name, email, gender,avatar;

	public FbUser(String id, String name, String email, String gender, String avatar) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.avatar = avatar;
	}

	public User toUser(){
		User user = new User();
		user.setLinkImage(avatar);
		user.setFullName(name);
		user.setEmail(email);
		user.setGender(gender);
		user.setIdUser(id);
		return user;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}

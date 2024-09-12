package com.shadypines.radio.view.authentication.signin.model;

import com.google.gson.annotations.SerializedName;

public class SignInResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	@SerializedName("isAdmin")
	private boolean isAdmin;

	@SerializedName("status")
	private int status;

	@SerializedName("username")
	private String username;

	@SerializedName("email")
	private String email;

	public boolean isAdmin(){
		return isAdmin;
	}

	public String username(){
		return username;
	}

	public int status(){
		return status;
	}

	public String email(){
		return email;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}


}
package com.shadypines.radio.view.authentication.signup.model;

import com.google.gson.annotations.SerializedName;

public class SignUpResponse{

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;



	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}
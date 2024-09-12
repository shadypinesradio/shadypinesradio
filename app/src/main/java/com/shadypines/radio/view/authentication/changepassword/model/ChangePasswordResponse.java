package com.shadypines.radio.view.authentication.changepassword.model;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse{

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
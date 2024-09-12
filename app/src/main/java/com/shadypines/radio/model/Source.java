package com.shadypines.radio.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Source{

	@SerializedName("relay")
	private Object relay;

	@SerializedName("type")
	private String type;

	@SerializedName("collaborator")
	private Object collaborator;

	public void setRelay(Object relay){
		this.relay = relay;
	}

	public Object getRelay(){
		return relay;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setCollaborator(Object collaborator){
		this.collaborator = collaborator;
	}

	public Object getCollaborator(){
		return collaborator;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"Source{" + 
			"relay = '" + relay + '\'' + 
			",type = '" + type + '\'' + 
			",collaborator = '" + collaborator + '\'' + 
			"}";
		}
}
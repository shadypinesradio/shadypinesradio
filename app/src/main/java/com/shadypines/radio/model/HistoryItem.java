package com.shadypines.radio.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class HistoryItem{

	@SerializedName("title")
	private String title;

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"HistoryItem{" + 
			"title = '" + title + '\'' + 
			"}";
		}
}
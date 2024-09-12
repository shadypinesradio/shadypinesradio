package com.shadypines.radio.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class CurrentTrack{

	@SerializedName("start_time")
	private String startTime;

	@SerializedName("artwork_url")
	private String artworkUrl;

	@SerializedName("artwork_url_large")
	private String artworkUrlLarge;

	@SerializedName("title")
	private String title;

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setArtworkUrl(String artworkUrl){
		this.artworkUrl = artworkUrl;
	}

	public String getArtworkUrl(){
		return artworkUrl;
	}

	public void setArtworkUrlLarge(String artworkUrlLarge){
		this.artworkUrlLarge = artworkUrlLarge;
	}

	public String getArtworkUrlLarge(){
		return artworkUrlLarge;
	}

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
			"CurrentTrack{" + 
			"start_time = '" + startTime + '\'' + 
			",artwork_url = '" + artworkUrl + '\'' + 
			",artwork_url_large = '" + artworkUrlLarge + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}
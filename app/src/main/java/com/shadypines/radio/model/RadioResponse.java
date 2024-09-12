package com.shadypines.radio.model;

import androidx.annotation.NonNull;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RadioResponse{
	@SerializedName("current_track")
	private CurrentTrack currentTrack;

	@SerializedName("logo_url")
	private String logoUrl;

	public void setCurrentTrack(CurrentTrack currentTrack){
		this.currentTrack = currentTrack;
	}

	public CurrentTrack getCurrentTrack(){
		return currentTrack;
	}

	public void setLogoUrl(String logoUrl){
		this.logoUrl = logoUrl;
	}

	public String getLogoUrl(){
		return logoUrl;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"RadioResponse{" +
			",current_track = '" + currentTrack + '\'' + 
			",logo_url = '" + logoUrl + '\'' +
			"}";
		}
}
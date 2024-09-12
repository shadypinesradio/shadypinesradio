package com.shadypines.radio.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class OutputsItem{

	@SerializedName("name")
	private String name;

	@SerializedName("format")
	private String format;

	@SerializedName("bitrate")
	private int bitrate;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setFormat(String format){
		this.format = format;
	}

	public String getFormat(){
		return format;
	}

	public void setBitrate(int bitrate){
		this.bitrate = bitrate;
	}

	public int getBitrate(){
		return bitrate;
	}

	@NonNull
	@Override
 	public String toString(){
		return 
			"OutputsItem{" + 
			"name = '" + name + '\'' + 
			",format = '" + format + '\'' + 
			",bitrate = '" + bitrate + '\'' + 
			"}";
		}
}
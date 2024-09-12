package com.shadypines.radio.model;
import com.google.gson.annotations.SerializedName;
public class RadioProgramResponse {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("Ads_Link")
    private String adsLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdsLink() {
        return adsLink;
    }

    public void setAdsLink(String adsLink) {
        this.adsLink = adsLink;
    }
}
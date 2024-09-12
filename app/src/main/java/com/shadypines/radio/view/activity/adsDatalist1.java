package com.shadypines.radio.view.activity;

import com.google.gson.annotations.SerializedName;


public class adsDatalist1 {
        @SerializedName("image")
        public String adsimage;
        @SerializedName("Ads_Link")
        public String adsLink;
        @SerializedName("Is_Show_Ads")
        public Boolean Is_Show_Ads;

        @SerializedName("Show_Ads_Everytime")
        public Boolean Show_Ads_Everytime;
}
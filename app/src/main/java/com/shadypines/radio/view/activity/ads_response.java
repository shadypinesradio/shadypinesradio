package com.shadypines.radio.view.activity;

import com.google.gson.annotations.SerializedName;

 public class ads_response {

        @SerializedName("success")
        private boolean success;

        @SerializedName("message")
        private String message;

        @SerializedName("data")
        public adsDatalist1 adsdata;


        public boolean isSuccess(){
            return success;
        }

        public String getMessage(){
            return message;
        }

}


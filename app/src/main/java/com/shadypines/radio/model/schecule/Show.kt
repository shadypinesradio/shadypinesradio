package com.shadypines.radio.model.schecule


import com.google.gson.annotations.SerializedName

data class Show(
    @SerializedName("djName")
    var djName: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("image")
    var image: String,
    @SerializedName("pushMessage")
    var pushMessage: String,
    @SerializedName("pushTitle")
    var pushTitle: String,
    @SerializedName("showDescription")
    var showDescription: String,
    @SerializedName("showName")
    var showName: String,
    @SerializedName("subscribers")
    var subscribers: Int,
    @SerializedName("url")
    var url: String?
)
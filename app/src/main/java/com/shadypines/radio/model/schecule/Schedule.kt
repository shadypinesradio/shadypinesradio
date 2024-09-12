package com.shadypines.radio.model.schecule


import com.google.gson.annotations.SerializedName

data class Schedule(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean
)
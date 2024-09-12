package com.shadypines.radio.model


import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("success")
    var success: Boolean,
    @SerializedName("isSubscribe")
    var isSubscribed:Boolean
)

data class RecoModel(
    @SerializedName("message")
    var message: String
)
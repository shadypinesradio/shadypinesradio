package com.shadypines.radio.model.schecule


import com.google.gson.annotations.SerializedName

data class Day(
    @SerializedName("dayName")
    var dayName: String,
    @SerializedName("end_time")
    var endTime: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("is_active")
    var isActive: Boolean,
    @SerializedName("is_deleteable")
    var isDeleteable: Boolean,
    @SerializedName("isSubscribe")
    var isSubscribe: Boolean,
    @SerializedName("showId")
    var showId: Int,
    @SerializedName("shows")
    var shows: List<Show>,
    @SerializedName("start_time")
    var startTime: String,
    @SerializedName("startTime")
    var startTimeStamp:Long,
    @SerializedName("endTime")
    var endTimeStamp:Long
)
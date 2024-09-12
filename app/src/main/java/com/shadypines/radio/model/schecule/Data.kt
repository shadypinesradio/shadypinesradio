package com.shadypines.radio.model.schecule


import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("Friday")
    var friday: List<Day>,
        @SerializedName("Monday")
    var monday: List<Day>,
        @SerializedName("Saturday")
    var saturday: List<Day>,
        @SerializedName("Sunday")
    var sunday: List<Day>,
        @SerializedName("Thursday")
    var thursday: List<Day>,
        @SerializedName("Tuesday")
    var tuesday: List<Day>,
        @SerializedName("Wednesday")
    var wednesday: List<Day>
)
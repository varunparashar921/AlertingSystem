package com.alertingsystem.core

import com.google.gson.annotations.SerializedName

class EarthQuake{

    @SerializedName("_id")
    var id: String = ""

    @SerializedName("originTime")
    var originTime: String = ""

    @SerializedName("magnitude")
    var magnitude: String = ""

    @SerializedName("epicenter")
    var epicenter: String = ""

    @SerializedName("lat")
    var lat: String = ""

    @SerializedName("lon")
    var lon: String = ""

}
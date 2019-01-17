package com.alertingsystem.core

import com.google.gson.annotations.SerializedName

class Flood{

    @SerializedName("_id")
    var id: String = ""

    @SerializedName("originTime")
    var originTime: String = ""

    @SerializedName("tips")
    var tips: String = ""

    @SerializedName("speed")
    var speed: String = ""

    @SerializedName("height")
    var height: String = ""

    @SerializedName("level")
    var level: String = ""

    @SerializedName("lat")
    var lat: String = ""

    @SerializedName("lon")
    var lon: String = ""

}
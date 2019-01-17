package com.alertingsystem.core

import com.google.gson.annotations.SerializedName

/**
 * Created by sgundavarapu on 24-02-2018.
 */
class Fire{

    @SerializedName("_id")
    var id: String = ""

    @SerializedName("originTime")
    var originTime: String = ""

    @SerializedName("temperature")
    var temperature: String = ""

    @SerializedName("lat")
    var lat: String = ""

    @SerializedName("lon")
    var lon: String = ""

}
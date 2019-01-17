package com.alertingsystem.core

import com.google.gson.annotations.SerializedName

/**
 * Created by sgundavarapu on 24-02-2018.
 */

class Tsunami {

    @SerializedName("_id")
    var id: String = ""

    @SerializedName("originTime")
    var originTime: String = ""

    @SerializedName("magnitude")
    var magnitude: String = ""

    @SerializedName("height")
    var height: String = ""

    @SerializedName("speed")
    var speed: String = ""

    @SerializedName("epicenter")
    var epicenter: String = ""

    @SerializedName("lat")
    var lat: String = ""

    @SerializedName("lon")
    var lon: String = ""

}

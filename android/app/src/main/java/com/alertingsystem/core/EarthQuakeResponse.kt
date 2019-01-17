package com.alertingsystem.core

import com.google.gson.annotations.SerializedName

class EarthQuakeResponse : ApiResponse() {

    @SerializedName("data")
    var data: List<EarthQuake>? = null

}
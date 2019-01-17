package com.alertingsystem.core

import com.google.gson.annotations.SerializedName

class FloodResponse : ApiResponse() {

    @SerializedName("data")
    var data: List<Flood>? = null


}
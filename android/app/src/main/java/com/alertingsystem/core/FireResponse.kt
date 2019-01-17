package com.alertingsystem.core

import com.google.gson.annotations.SerializedName

/**
 * Created by sgundavarapu on 24-02-2018.
 */
class FireResponse : ApiResponse(){

    @SerializedName("data")
    var data: List<Fire>? = null

}
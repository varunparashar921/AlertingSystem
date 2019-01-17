package com.alertingsystem.core

import com.google.gson.annotations.SerializedName

open class ApiResponse {

    @SerializedName("statusCode")
    var status: Int = 0

    @SerializedName("message")
    var message: String? = null

}

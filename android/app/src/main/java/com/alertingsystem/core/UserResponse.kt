package com.alertingsystem.core

import com.google.gson.annotations.SerializedName

/**
 * Created by sgundavarapu on 28-10-2017.
 */

class UserResponse : ApiResponse() {

    @SerializedName("data")
    var user: User? = null

}
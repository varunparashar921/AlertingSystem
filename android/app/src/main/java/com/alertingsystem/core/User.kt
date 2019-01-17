package com.alertingsystem.core

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class User() : Parcelable{

    @SerializedName("_id")
    var userId: String? = null
    @SerializedName("firstName")
    var firstName: String? = null
    @SerializedName("lastName")
    var lastName: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("gender")
    var gender: String? = null
    @SerializedName("phNo")
    var phNo: String? = null
    @SerializedName("password")
    var password: String? = null
    @SerializedName("notifyNotification")
    var notifyNotification: Boolean = false
    @SerializedName("lat")
    var lat: String? = null
    @SerializedName("lon")
    var lon: String? = null

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        firstName = parcel.readString()
        lastName = parcel.readString()
        email = parcel.readString()
        gender = parcel.readString()
        phNo = parcel.readString()
        password = parcel.readString()
        notifyNotification = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeString(gender)
        parcel.writeString(phNo)
        parcel.writeString(password)
        parcel.writeByte(if (notifyNotification) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}

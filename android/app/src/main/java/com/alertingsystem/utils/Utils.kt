package com.alertingsystem.utils

import com.alertingsystem.AlertingSystemApp
import com.alertingsystem.core.User

object Utils {

    val user: User
        get() {
            val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
            val user = User()
            user.userId = sharedPreferences.getString(AppConstants.USER_ID, null)
            user.firstName = sharedPreferences.getString(AppConstants.USER_FIRST_NAME, null)
            user.lastName = sharedPreferences.getString(AppConstants.USER_LAST_NAME, null)
            user.email = sharedPreferences.getString(AppConstants.USER_MAIL_ID, null)
            user.gender = sharedPreferences.getString(AppConstants.USER_GENDER, null)
            user.phNo = sharedPreferences.getString(AppConstants.USER_PHNO, null)
            user.lat = sharedPreferences.getString(AppConstants.USER_LAT, null)
            user.lon = sharedPreferences.getString(AppConstants.USER_LON, null)
            user.notifyNotification = sharedPreferences.getBoolean(AppConstants.USER_NOTIFICATION_ON, true)
            return user
        }

    fun saveUser(user: User) {
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        sharedPreferences.edit()
                .putString(AppConstants.USER_ID, user.userId)
                .putString(AppConstants.USER_FIRST_NAME, user.firstName)
                .putString(AppConstants.USER_LAST_NAME, user.lastName)
                .putString(AppConstants.USER_MAIL_ID, user.email)
                .putString(AppConstants.USER_GENDER, user.gender)
                .putString(AppConstants.USER_PHNO, user.phNo)
                .putString(AppConstants.USER_LAT, user.lat)
                .putString(AppConstants.USER_LON, user.lon)
                .putBoolean(AppConstants.USER_NOTIFICATION_ON, user.notifyNotification)
                .apply()
    }

    fun distance(lat1: Double, lng1: Double,
                 lat2: Double, lng2: Double): Double {
        val a = (lat1 - lat2) * distPerLat(lat1)
        val b = (lng1 - lng2) * distPerLng(lat1)
        return Math.sqrt(a * a + b * b)/1000
    }

    private fun distPerLng(lat: Double): Double {
        return (0.0003121092 * Math.pow(lat, 4.0) + 0.0101182384 * Math.pow(lat, 3.0) - 17.2385140059 * lat * lat
                + 5.5485277537 * lat + 111301.967182595)
    }

    private fun distPerLat(lat: Double): Double {
        return -0.000000487305676 * Math.pow(lat, 4.0) - 0.0033668574 * Math.pow(lat, 3.0) + 0.4601181791 * lat * lat - 1.4558127346 * lat + 110579.25662316
    }

}
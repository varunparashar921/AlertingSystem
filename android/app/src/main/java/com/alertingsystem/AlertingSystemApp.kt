package com.alertingsystem

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.alertingsystem.utils.AppConstants
import okhttp3.internal.Internal.instance

class AlertingSystemApp : Application() {

    val sharedPreferences: SharedPreferences
        get() = super.getSharedPreferences(AppConstants.APP_PREF, Context.MODE_PRIVATE)

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        lateinit var instance: AlertingSystemApp
            private set

    }

}

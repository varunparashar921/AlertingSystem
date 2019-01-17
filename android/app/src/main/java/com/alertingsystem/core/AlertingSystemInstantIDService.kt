package com.alertingsystem.core

import android.util.Log
import android.widget.Toast
import com.alertingsystem.AlertingSystemApp
import com.alertingsystem.R
import com.alertingsystem.utils.AppConstants
import com.alertingsystem.utils.RetrofitClient
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlertingSystemInstantIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "RefreshedToken:" + refreshedToken!!)
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        val isLoggedIn = sharedPreferences.getBoolean(AppConstants.IS_LOGGED_IN, false)
        if (isLoggedIn) {
            sendRegistrationToServer(refreshedToken)
        }
    }

    private fun sendRegistrationToServer(refreshedToken: String?) {
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        val userId = sharedPreferences.getString(AppConstants.USER_ID, null)
        val familyLinkService = RetrofitClient.getClient().create(AlertingSystemService::class.java)
        val createUserService = familyLinkService.sendToken(userId!!, refreshedToken!!)
        createUserService.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse!!.status == 200 || userResponse.status == 503) {
                        Toast.makeText(AlertingSystemApp.instance, userResponse.message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {

        private val TAG = AlertingSystemInstantIDService::class.java.simpleName
    }

}

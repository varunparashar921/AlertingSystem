package com.alertingsystem.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alertingsystem.AlertingSystemApp
import com.alertingsystem.R
import com.alertingsystem.activities.LoginActivity
import com.alertingsystem.core.AlertingSystemService
import com.alertingsystem.core.ApiResponse
import com.alertingsystem.utils.AppConstants
import com.alertingsystem.utils.RetrofitClient
import com.alertingsystem.utils.Utils
import com.alertingsystem.utils.Utils.user
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.fragment_setting.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsFragment : Fragment() {

    private val PLACE_PICKER_REQUEST=123

    private lateinit var mainView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView=view
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        view.logoutButton.setOnClickListener {
            sharedPreferences.edit().clear().commit()
            activity.finish()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        val user = Utils.user
        view.latEditText.setText(user.lat)
        view.lonEditText.setText(user.lon)
        view.updateButton.setOnClickListener {
            val lat = view.latEditText.text.toString()
            val lon = view.lonEditText.text.toString()
            val progressDialog = ProgressDialog.show(activity, getString(R.string.loading), null, true, false)
            val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
            val familyLinkService = RetrofitClient.getClient().create(AlertingSystemService::class.java)
            val createUserService = familyLinkService.changeLocation(user.userId!!, lat,lon)
            createUserService.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        val tsunamiResponse = response.body()
                        if (tsunamiResponse!!.status == 200 || tsunamiResponse.status == 503) {
                            sharedPreferences.edit()
                                    .putString(AppConstants.USER_LAT, lat)
                                    .putString(AppConstants.USER_LON, lon)
                                    .apply()
                            Toast.makeText(activity,"Updated Successfully",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            })
        }
        view.pickButton.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
        }
        val isNotificationOn = sharedPreferences.getBoolean(AppConstants.USER_NOTIFICATION_ON, true)
        view.notifyCheckBox.isChecked = isNotificationOn
        view.notifyCheckBox.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit()
                    .putBoolean(AppConstants.USER_NOTIFICATION_ON, isChecked)
                    .apply()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(activity,data)
                val toastMsg = String.format("Place: %s", place.getName())
                mainView.latEditText.setText(place.latLng.latitude.toString())
                mainView.lonEditText.setText(place.latLng.longitude.toString())
                Toast.makeText(activity, toastMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

}

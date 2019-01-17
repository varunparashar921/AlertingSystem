package com.alertingsystem.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.alertingsystem.R
import com.alertingsystem.core.AlertingSystemService
import com.alertingsystem.core.UserResponse
import com.alertingsystem.utils.RetrofitClient
import com.alertingsystem.utils.Utils.saveUser
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegistrationActivity : BaseActivity(), View.OnFocusChangeListener, View.OnClickListener {

    private var progressDialog: ProgressDialog? = null
    private val PLACE_PICKER_REQUEST: Int = 143

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        first_name_et.setOnFocusChangeListener(this)
        last_name_et.setOnFocusChangeListener(this)
        email_et.setOnFocusChangeListener(this)
        pwd_et.setOnFocusChangeListener(this)
        phno_et.setOnFocusChangeListener(this)
        register_btn.setOnClickListener(this)
        pickButton.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this@RegistrationActivity), PLACE_PICKER_REQUEST)
        }
    }

    override fun onClick(v: View?) {
        val email = email_et.text.toString().trim({ it <= ' ' })
        if (!isValidEmailId(email)) {
            email_et.error = "Invalid EmailId"
            return
        }
        // Check for a valid password, if the user entered one.
        val pwd = pwd_et.getText().toString()
        if (!isPasswordValid(pwd)) {
            pwd_et.error = getString(R.string.error_invalid_password)
            return
        }
        val phNo = phno_et.getText().toString()
        if (!isValidPhNo(phNo)) {
            phno_et.error = getString(R.string.error_invalid_phNo)
            return
        }
        val firstName = first_name_et.getText().toString()
        if (TextUtils.isEmpty(firstName)) {
            first_name_et.setError(getString(R.string.error_first_name))
            return
        }
        val lastName = last_name_et.getText().toString()
        if (TextUtils.isEmpty(lastName)) {
            last_name_et.setError(getString(R.string.error_last_name))
            return
        }
        val lat = latEditText.text.toString()
        val lon = lonEditText.text.toString()

        val refreshedToken = FirebaseInstanceId.getInstance().token
        progressDialog = ProgressDialog.show(this, getString(R.string.loading), null, true, false)
        val alertingSystemService = RetrofitClient.getClient().create(AlertingSystemService::class.java)
        val createUserService = alertingSystemService
                .createUser(firstName, lastName, email, pwd, phNo, "mydevice", "Android", refreshedToken
                        ?: "", lat, lon)
        createUserService.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                progressDialog?.dismiss()
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse?.status == 200) {
                        saveUser(userResponse.user!!)
                        val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    } else if (userResponse?.status == 503) {
                        Toast.makeText(this@RegistrationActivity, userResponse.message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@RegistrationActivity, R.string.oops_msg, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                progressDialog?.dismiss()
                Toast.makeText(this@RegistrationActivity, R.string.oops_msg, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                val toastMsg = String.format("Place: %s", place.getName())
                latEditText.setText(place.latLng.latitude.toString())
                lonEditText.setText(place.latLng.longitude.toString())
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v === email_et) {
            val email = email_et.text.toString().trim({ it <= ' ' })
            if (!isValidEmailId(email) && !hasFocus) {
                email_et.error = getString(R.string.error_invalid_email)
            }
        } else if (v === pwd_et) {
            val pwd = pwd_et.text.toString()
            if (!isPasswordValid(pwd) && !hasFocus) {
                pwd_et.error = getString(R.string.error_invalid_password)
            }
        } else if (v === phno_et) {
            val phNo = phno_et.text.toString()
            if (!isValidPhNo(phNo) && !hasFocus) {
                phno_et.error = getString(R.string.error_invalid_phNo)
            }
        } else if (v === first_name_et) {
            val firstName = first_name_et.getText().toString()
            if (TextUtils.isEmpty(firstName) && !hasFocus) {
                first_name_et.setError(getString(R.string.error_first_name))
            }
        } else if (v === last_name_et) {
            val lastName = last_name_et.getText().toString()
            if (TextUtils.isEmpty(lastName) && !hasFocus) {
                last_name_et.setError(getString(R.string.error_last_name))
            }
        }
    }

    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches()
    }

    private fun isValidPhNo(phNo: String): Boolean {
        return phNo.length >= 10
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}

package com.alertingsystem.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.alertingsystem.AlertingSystemApp
import com.alertingsystem.R
import com.alertingsystem.core.AlertingSystemService
import com.alertingsystem.core.ApiResponse
import com.alertingsystem.fragments.HistoryFragment
import com.alertingsystem.fragments.HomeFragment
import com.alertingsystem.fragments.SettingsFragment
import com.alertingsystem.utils.AppConstants
import com.alertingsystem.utils.RetrofitClient
import com.alertingsystem.utils.Utils.user
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private lateinit var tabAdapter: TabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        tabAdapter = TabAdapter(supportFragmentManager)
        viewPager.adapter = tabAdapter
        viewPager.offscreenPageLimit = tabAdapter.count
        viewPager.addOnPageChangeListener(this)
        tabLayout.setupWithViewPager(viewPager)

        FirebaseMessaging.getInstance().subscribeToTopic("earthquake")
        FirebaseMessaging.getInstance().subscribeToTopic("flood")
        FirebaseMessaging.getInstance().subscribeToTopic("tsunami")
        FirebaseMessaging.getInstance().subscribeToTopic("fire")

        val refreshedToken = FirebaseInstanceId.getInstance().token
        sendRegistrationToServer(refreshedToken ?: "")
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        val familyLinkService = RetrofitClient.getClient().create(AlertingSystemService::class.java)
        val createUserService = familyLinkService.sendToken(user.userId ?: "", refreshedToken)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.logout) {
            val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
            sharedPreferences.edit().putBoolean(AppConstants.IS_LOGGED_IN, false).apply()
            finish()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        title = tabAdapter.getPageTitle(position)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    inner class TabAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        private var fragments = arrayOf(HomeFragment(), HistoryFragment(), SettingsFragment())

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Home"
                1 -> "History"
                2 -> "Settings"
                else -> ""
            }
        }

    }

}

package com.alertingsystem.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alertingsystem.AlertingSystemApp
import com.alertingsystem.R
import com.alertingsystem.adapters.EarthQuakeAdapter
import com.alertingsystem.adapters.FireAdapter
import com.alertingsystem.adapters.FloodAdapter
import com.alertingsystem.adapters.TsunamiAdapter
import com.alertingsystem.core.*
import com.alertingsystem.utils.AppConstants
import com.alertingsystem.utils.RetrofitClient
import kotlinx.android.synthetic.main.fragment_history.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var mainView: View

    val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (mainView != null) {
                intent?.let {
                    when (it.action) {
                        AppConstants.ACTION_UPDATE_EARTH_QUAKE -> getEarthQuakeData()
                        AppConstants.ACTION_UPDATE_FIRE_QUAKE -> getFireData()
                        AppConstants.ACTION_UPDATE_FLOOD -> getFloodData()
                        AppConstants.ACTION_UPDATE_TSUNAMI -> getTsunamiData()
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = view
        mainView.tsunamiRecyclerView.isNestedScrollingEnabled = false
        mainView.earthquakeRecyclerView.isNestedScrollingEnabled = false
        mainView.fireRecyclerView.isNestedScrollingEnabled = false
        mainView.floodRecyclerView.isNestedScrollingEnabled = false

        val intentFilter = IntentFilter(AppConstants.ACTION_UPDATE_EARTH_QUAKE)
        intentFilter.addAction(AppConstants.ACTION_UPDATE_TSUNAMI)
        intentFilter.addAction(AppConstants.ACTION_UPDATE_FLOOD)
        intentFilter.addAction(AppConstants.ACTION_UPDATE_FIRE_QUAKE)
        LocalBroadcastManager.getInstance(activity).registerReceiver(mReceiver, intentFilter)
    }

    override fun onDestroyView() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiver)
        super.onDestroyView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            loadHistory()
        }
    }

    private fun loadHistory() {
        getTsunamiData()
        getEarthQuakeData()
        getFireData()
        getFloodData()
    }

    private fun getFloodData() {
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        val lat = sharedPreferences.getString(AppConstants.USER_LAT, "")
        val lon = sharedPreferences.getString(AppConstants.USER_LON, "")
        val familyLinkService = RetrofitClient.getClient().create(AlertingSystemService::class.java)
        val createUserService = familyLinkService.getFloodHistory(lat, lon)
        createUserService.enqueue(object : Callback<FloodResponse> {
            override fun onResponse(call: Call<FloodResponse>, response: Response<FloodResponse>) {
                if (response.isSuccessful) {
                    val tsunamiResponse = response.body()
                    if (tsunamiResponse!!.status == 200 || tsunamiResponse.status == 503) {
                        val data = tsunamiResponse.data
                        if (!data!!.isEmpty()) {
                            mainView.floodRecyclerView.layoutManager = LinearLayoutManager(activity)
                            mainView.floodRecyclerView.adapter = FloodAdapter(data, activity)
                            mainView.floodEmptyTextView.visibility = View.GONE
                        } else {
                            mainView.floodEmptyTextView.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<FloodResponse>, t: Throwable) {
                Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getFireData() {
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        val lat = sharedPreferences.getString(AppConstants.USER_LAT, "")
        val lon = sharedPreferences.getString(AppConstants.USER_LON, "")
        val familyLinkService = RetrofitClient.getClient().create(AlertingSystemService::class.java)
        val createUserService = familyLinkService.getFireHistory(lat, lon)
        createUserService.enqueue(object : Callback<FireResponse> {
            override fun onResponse(call: Call<FireResponse>, response: Response<FireResponse>) {
                if (response.isSuccessful) {
                    val tsunamiResponse = response.body()
                    if (tsunamiResponse!!.status == 200 || tsunamiResponse.status == 503) {
                        val data = tsunamiResponse.data
                        if (!data!!.isEmpty()) {
                            mainView.fireRecyclerView.layoutManager = LinearLayoutManager(activity)
                            mainView.fireRecyclerView.adapter = FireAdapter(data, activity)
                            mainView.fireEmptyTextView.visibility = View.GONE
                        } else {
                            mainView.fireEmptyTextView.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<FireResponse>, t: Throwable) {
                Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getEarthQuakeData() {
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        val lat = sharedPreferences.getString(AppConstants.USER_LAT, "")
        val lon = sharedPreferences.getString(AppConstants.USER_LON, "")
        val familyLinkService = RetrofitClient.getClient().create(AlertingSystemService::class.java)
        val createUserService = familyLinkService.getEarthQuakeHistory(lat, lon)
        createUserService.enqueue(object : Callback<EarthQuakeResponse> {
            override fun onResponse(call: Call<EarthQuakeResponse>, response: Response<EarthQuakeResponse>) {
                if (response.isSuccessful) {
                    val earthQuakeResponse = response.body()
                    if (earthQuakeResponse!!.status == 200 || earthQuakeResponse.status == 503) {
                        val data = earthQuakeResponse.data
                        if (!data!!.isEmpty()) {
                            mainView.earthquakeRecyclerView.layoutManager = LinearLayoutManager(activity)
                            mainView.earthquakeRecyclerView.adapter = EarthQuakeAdapter(data, activity)
                            mainView.earthqualeEmptyTextView.visibility = View.GONE
                        } else {
                            mainView.earthqualeEmptyTextView.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<EarthQuakeResponse>, t: Throwable) {
                Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getTsunamiData() {
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        val lat = sharedPreferences.getString(AppConstants.USER_LAT, "")
        val lon = sharedPreferences.getString(AppConstants.USER_LON, "")
        val familyLinkService = RetrofitClient.getClient().create(AlertingSystemService::class.java)
        val createUserService = familyLinkService.getTsunamiHistory(lat, lon)
        createUserService.enqueue(object : Callback<TSunamiResponse> {
            override fun onResponse(call: Call<TSunamiResponse>, response: Response<TSunamiResponse>) {
                if (response.isSuccessful) {
                    val tsunamiResponse = response.body()
                    if (tsunamiResponse!!.status == 200 || tsunamiResponse.status == 503) {
                        val data = tsunamiResponse.data
                        if (!data!!.isEmpty()) {
                            mainView.tsunamiRecyclerView.layoutManager = LinearLayoutManager(activity)
                            mainView.tsunamiRecyclerView.adapter = TsunamiAdapter(data, activity)
                            mainView.tsunamiEmptyTextView.visibility = View.GONE
                        } else {
                            mainView.tsunamiEmptyTextView.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<TSunamiResponse>, t: Throwable) {
                Toast.makeText(AlertingSystemApp.instance, R.string.oops_msg, Toast.LENGTH_LONG).show()
            }
        })
    }

}
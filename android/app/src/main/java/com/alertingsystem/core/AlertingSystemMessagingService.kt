package com.alertingsystem.core

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.util.Log
import com.alertingsystem.AlertingSystemApp
import com.alertingsystem.R
import com.alertingsystem.activities.MainActivity
import com.alertingsystem.utils.AppConstants
import com.alertingsystem.utils.Utils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class AlertingSystemMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        Log.d(TAG, "RemoteMessage: " + remoteMessage.from)
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        val userId = sharedPreferences.getString(AppConstants.USER_ID, null)
        if (TextUtils.isEmpty(userId)) {
            return
        }
        val isNotificationOn = sharedPreferences.getBoolean(AppConstants.USER_NOTIFICATION_ON, true)
        // Check if message contains a data payload.
        var obj:Any?= null
        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data
            when (data["type"]) {
                "earthquick" -> {
                    obj = Gson().fromJson<EarthQuake>(data["body"], EarthQuake::class.java)
                }
                "fire" -> {
                    obj = Gson().fromJson<Fire>(data["body"], Fire::class.java)
                }
                "tsunami" -> {
                    obj = Gson().fromJson<Tsunami>(data["body"], Tsunami::class.java)
                }
                "flood" -> {
                    obj = Gson().fromJson<Flood>(data["body"], Flood::class.java)
                }
            }

            if (remoteMessage.notification != null && isNotificationOn && obj!=null && isAvailableLoc(obj)) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
                sendNotification(remoteMessage.notification!!.title,
                        remoteMessage.notification!!.body, intent)
                when (data["type"]) {
                    "earthquick" -> {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(AppConstants.ACTION_UPDATE_EARTH_QUAKE))
                    }
                    "fire" -> {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(AppConstants.ACTION_UPDATE_FIRE_QUAKE))
                    }
                    "tsunami" -> {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(AppConstants.ACTION_UPDATE_TSUNAMI))
                    }
                    "flood" -> {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(AppConstants.ACTION_UPDATE_FLOOD))
                    }
                }
            }
        }
    }

    private fun isAvailableLoc(any: Any): Boolean {
        val sharedPreferences = AlertingSystemApp.instance.sharedPreferences
        val lat = sharedPreferences.getString(AppConstants.USER_LAT, "")
        val lon = sharedPreferences.getString(AppConstants.USER_LON, "")
        if (lat.isNotEmpty() && lon.isNotEmpty()) {
            if (any is EarthQuake) {
                return Utils.distance(any.lat.toDouble(), any.lon.toDouble(), lat.toDouble(), lon.toDouble()) < 50
            } else if (any is Fire) {
                return Utils.distance(any.lat.toDouble(), any.lon.toDouble(), lat.toDouble(), lon.toDouble()) < 50
            } else if (any is Flood) {
                return Utils.distance(any.lat.toDouble(), any.lon.toDouble(), lat.toDouble(), lon.toDouble()) < 50
            } else if (any is Tsunami) {
                return Utils.distance(any.lat.toDouble(), any.lon.toDouble(), lat.toDouble(), lon.toDouble()) < 50
            }
        }
        return false
    }

    private fun sendNotification(title: String?, message: String?, intent: Intent) {
        Log.d(TAG, "Notification:::::" + message!!)
        val pendingintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
        if (pendingintent != null) {
            notificationBuilder.setContentIntent(pendingintent)
        }
        val notification = notificationBuilder.build()
        notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
        notification.defaults = notification.defaults or Notification.DEFAULT_VIBRATE
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {

        private val TAG = AlertingSystemMessagingService::class.java.simpleName
    }

}

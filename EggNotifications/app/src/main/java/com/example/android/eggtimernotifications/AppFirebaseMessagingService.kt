package com.example.android.eggtimernotifications

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.android.eggtimernotifications.util.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d("FLMWG", "Remote Message From -> ${remoteMessage?.from}")

        remoteMessage?.data?.let { Log.d("FLMWG", "Remote Message Data Payload -> ${remoteMessage.data}") }

        remoteMessage?.notification?.let {
            Log.d("FLMWG", "Remote Message Notification Body: ${it.body}")
            sendNotification(it.body!!)
        }
    }

    private fun sendNotification(msgBody: String) {
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
        notificationManager?.sendNotification(msgBody, applicationContext)
    }

    override fun onNewToken(token: String?) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d("FLMWG", "FCM Token -> $token")
    }
}
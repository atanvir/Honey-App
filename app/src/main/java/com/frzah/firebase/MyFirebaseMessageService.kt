package com.frzah.firebase

import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import com.frzah.activity.Order.OrderActivity
import com.frzah.utils.SharedPreferenceUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MyFirebaseMessageService : FirebaseMessagingService(){
    var TAG = MyFirebaseMessageService::class.java.simpleName
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "data:" + remoteMessage.data)
        Log.e(TAG, "notification:" + remoteMessage.notification)
        if (remoteMessage.data.size > 0) handleMessage(remoteMessage.data["title"],
            remoteMessage.data["body"])
        else if (remoteMessage.notification != null) handleMessage(remoteMessage.notification!!.title,
            remoteMessage.notification!!.body)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        SharedPreferenceUtil.getInstance(applicationContext).device_token=p0
    }

    private fun handleMessage(title: String?, body: String?) {
        val broadcastIntent = Intent("com.honey")
        sendBroadcast(broadcastIntent)

        val intent = Intent(getApplicationContext(), OrderActivity::class.java)
        intent.putExtra("cameFrom", MyFirebaseMessageService::class.simpleName)
        intent.putExtra("body", body)
        showNotificationMessage(getApplicationContext(), title, body, intent, Random().nextInt())
        playNotificationSound()
    }

    fun showNotificationMessage(context: Context?, title: String?, message: String?, intent: Intent?, id: Int) {
        val notificationHelper = NotificationHelper(context!!)
        notificationHelper.createNotification(title, message, intent, id)
    }

    fun playNotificationSound() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(this, notification)
            r.play()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
package com.frzah.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.frzah.R

class NotificationHelper(private val mContext: Context) : ContextWrapper(mContext) {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "1001"
    }

    init {
        createNotificationChannel()
    }


    fun createNotification(title: String?, message: String?, intent: Intent?, id: Int) {
        val mBuilder = NotificationCompat.Builder(mContext.applicationContext, "notify_001")
        val pendingIntent = PendingIntent.getActivity(mContext, id, intent, PendingIntent.FLAG_ONE_SHOT)
        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(message)
        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.icon)
        mBuilder.setContentTitle(title)
        mBuilder.color = ContextCompat.getColor(mContext, R.color.app_theme_organe)
        mBuilder.setContentText(message)
        mBuilder.setAutoCancel(true)
        mBuilder.priority = Notification.PRIORITY_MAX
        mBuilder.setStyle(bigText)
        val mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("notify_001", "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            mNotificationManager.createNotificationChannel(channel)
        }
        mNotificationManager.notify(0, mBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "LikeWise"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

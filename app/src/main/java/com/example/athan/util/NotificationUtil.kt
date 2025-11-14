package com.example.athan.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationUtil {
    const val CHANNEL_NAME = "NIII_ATHAN_NAME"
    const val CHANNEL_ID = "ATHAN_NAME_ID"
    const val CHANNEL_NOTIFICATION_ID = 4444

    fun notificationMannerHolder(context:Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }
    fun createNotificationChannel(context: Context){

       val notificationManger = notificationMannerHolder(context)

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManger.createNotificationChannel(channel)
        }
    }
}
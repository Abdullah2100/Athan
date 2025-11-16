package com.example.athan.Broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat.startForegroundService
import com.example.athan.alarm.AlarmSchedule.Companion.ALARM_ACTION
import com.example.athan.services.AthanServices

class AlarmBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(ALARM_ACTION)) {
            val athanName = intent?.getStringExtra("name") ?: ""
            val athanSound = intent?.getStringExtra("url")
            val services = Intent(context, AthanServices::class.java).apply {
                putExtra("name",athanName)
                putExtra("url", athanSound)
            }
            context?.let {
                    startForegroundService(context,services)
            }
        }
    }
}
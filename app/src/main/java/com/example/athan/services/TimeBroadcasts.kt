package com.example.athan.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.athan.util.UpdateAthanTime
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TimeBroadcasts : BroadcastReceiver() {

    @Inject
    lateinit var updateNextAthanObjectFun: UpdateAthanTime

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Intent.ACTION_TIME_TICK)) {
            updateNextAthanObjectFun.updateNextAthanObjectFun()
        }
    }



}
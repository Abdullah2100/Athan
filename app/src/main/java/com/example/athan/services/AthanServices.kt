package com.example.athan.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.athan.R
import com.example.athan.util.NotificationUtil
import com.example.athan.util.NotificationUtil.CHANNEL_ID


class AthanServices : Service() {
    var mediaPlayer: MediaPlayer? = null


    companion object {
        const val CLOSE_NOTIFICATION = "CLOSE_NOTIFICATION"
        const val FOREGROUND_SERVICE_ID = 55555
    }

    override fun onBind(intent: Intent?): IBinder? = null


    fun playSound(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.azan1)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            stopSound()
        }
    }

    fun stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            stopSelf()

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(intent?.getStringExtra(CLOSE_NOTIFICATION)==CLOSE_NOTIFICATION){
            stopSound()
            return START_NOT_STICKY
        }

        startForeground(FOREGROUND_SERVICE_ID, createNotification(this, intent))

        playSound(this)

        return START_STICKY;
    }


    fun createNotification(context: Context, intent: Intent?): Notification {
        val athanName = intent?.getStringExtra("name") ?: ""

        val closeIntent = Intent(context,AthanServices::class.java).apply {
            putExtra(CLOSE_NOTIFICATION, CLOSE_NOTIFICATION)
        }

        val pendingClose = PendingIntent.getService(
            context,
            1,
            closeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        NotificationUtil.createNotificationChannel(context)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("الان وقت الاذان")
            .setContentText("الاذان الان هو اذان $athanName")
            .addAction(
                R.drawable.ic_launcher_foreground,
                "اغلاق الاذان",
                pendingClose
            )
            .build()
    }

}

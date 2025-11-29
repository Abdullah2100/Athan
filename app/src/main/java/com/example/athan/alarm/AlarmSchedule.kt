package com.example.athan.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.athan.Broadcast.AlarmBroadCast
import com.example.athan.data.local.entity.Time
import com.example.athan.data.repository.AthanRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AlarmSchedule @Inject constructor(
    @ApplicationContext val context: Context,
    private val alarmManager: AlarmManager,
    private val athanRepository: AthanRepository
) {

    companion object {
        const val ALARM_ACTION = "com.example_ALERM_ATHAN_TIME"
    }

    @SuppressLint("ScheduleExactAlarm")
    suspend fun scheduleNextAthan(nextAthanTime: Time) {
        val currentHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, nextAthanTime.hour)
            set(Calendar.MINUTE, nextAthanTime.minute)

            set(Calendar.SECOND, 0)

            set(Calendar.MILLISECOND, 0)

            if (nextAthanTime.name == "Fajr" && currentHour.hour > 18) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        val savedAthan = athanRepository.getSavedAthan()
        val intent = Intent(context, AlarmBroadCast::class.java).apply {
            action = ALARM_ACTION
            putExtra("name", nextAthanTime.name)
            putExtra("url", savedAthan?.path)
        }


        val pending = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


//       alarmManager.setAlarmClock(
//           AlarmManager.AlarmClockInfo(calender.timeInMillis, null),
//           pending
//        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calender.timeInMillis,
            pending
        )

    }

}
package com.example.athan.util

import android.annotation.SuppressLint
import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object General {

    fun bastUr(longitude:Double,
               latitude:Double,
               from:String,
               to:String):String{
        val timeZon = getCurrentTimeZone();
        return "http://api.islamhouse.com/v1/XXXXX/services/praytime/get-times/$from/$to/Makkah/$latitude/$longitude/$timeZon/json"
    }


    private fun getCurrentTimeZone():String{
        val timeZone = TimeZone.getDefault()
        val zoneOffset= timeZone.getOffset(Calendar.getInstance().timeInMillis)
        val offsetHours = zoneOffset / (60 * 60 * 1000)
        return if(offsetHours>0)"+$offsetHours" else offsetHours.toString()
    }

    fun getCurrentDate(nextDay: Int? = null): String {
        val c = Calendar.getInstance()
        if (nextDay != null) c.add(Calendar.DATE, nextDay)
        return "${c.get(Calendar.YEAR)}-${c.get(Calendar.MONTH)+1}-${c.get(Calendar.DAY_OF_MONTH)}"
    }



  fun   List<Int>.toValidDayHour():String
  {
      val hour = this[0]
      val Minute = this[1]
      val isMorning = if(hour<12)true else false
      val newHour = if(!isMorning)hour-12 else hour
      return "$newHour:$Minute ${if(isMorning)"ص" else "م"}"
  }

    @SuppressLint("SimpleDateFormat")
    fun String.toDate(): Date?{
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.parse(this)
    }


}
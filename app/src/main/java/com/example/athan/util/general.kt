package com.example.athan.util

import android.icu.util.Calendar
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
        return TimeZone.getDefault().displayName;
    }

    fun getCurrentDate(nextDay: Int? = null): String {
        val c = Calendar.getInstance()
        if (nextDay != null) c.add(Calendar.DATE, nextDay)
        return "${c.get(Calendar.YEAR)}-${c.get(Calendar.MONTH)}-${c.get(Calendar.DAY_OF_MONTH)}"
    }
}
package com.example.athan.util

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
}
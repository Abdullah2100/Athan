package com.example.athan.util

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.athan.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
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



  fun   List<Int>.toValidDayHour(context: Context):String
  {
      val hour = this[0]
      val Minute = this[1]
      val isMorning = if(hour<12)true else false
      val newHour = if(!isMorning)hour-12 else hour
      return "$newHour:$Minute ${if(isMorning) context.getString(R.string.am) else context.getString(R.string.pm)}"
  }

    fun Uri.toCustomFil(context: Context): File? {
        val contentResolver = context.contentResolver
        val fileName = contentResolver.query(this, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    cursor.getString(displayNameIndex)
                } else {
                    null
                }
            } else {
                null
            }
        } ?: "temp_audio_file"

        val file = File(context.cacheDir, fileName)
        try {
            val inputStream = contentResolver.openInputStream(this)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            Log.d("errorFromSelectAudio", e.message.toString())
            return null
        }
        return file
    }

    @SuppressLint("SimpleDateFormat")
    fun String.toDate(): Date?{
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.parse(this)
    }


}
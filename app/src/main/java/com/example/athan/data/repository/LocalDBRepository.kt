package com.example.athan.data.repository

import com.example.athan.data.local.dao.DateDao
import com.example.athan.data.local.dao.TimeDao
import com.example.athan.data.local.entity.Date
import com.example.athan.data.local.entity.Time
import com.example.athan.data.network.dto.AthanDaysDto
import com.example.athan.model.AthanDay
import com.example.athan.model.AthanTime
import com.example.athan.util.General
import javax.inject.Inject

class LocalDBRepository @Inject constructor(
    private val dateDao: DateDao,
    private val timeDao: TimeDao
) {

    //this to save athan from api to room database
    suspend fun saveAthanDateDayAndTime(days: List<String>, timeData: List<List<String>>) {
        days.forEachIndexed { index, dayData ->
            val date = Date(null, dayData);
            dateDao.insertDate(date)
            val timeData = timeData[index];
            val dateId = dateDao.getDateByCurrentDay(date.date)?.id;
            if(dateId==null) return@forEachIndexed;
            timeData.forEachIndexed { timeIndex, time ->
                val hourMinute = time.split(":")
                val athanTime = when (timeIndex) {
                    0 -> {
                        Time(
                            null,
                            dateId = dateId,
                            hour = (hourMinute[0]).toInt(),
                            minute = (hourMinute[1]).toInt(),
                            isMainPray = true,
                            name = "Fajr",
                            date = date.date
                        )
                    }

                    1 -> {
                        Time(
                            null,
                            dateId = dateId,
                            hour = (hourMinute[0]).toInt(),
                            minute = (hourMinute[1]).toInt(),
                            isMainPray = false,
                            name = "Sunrise",
                            date = date.date
                        )
                    }

                    2 -> {
                        Time(
                            null,
                            dateId = dateId,
                            hour = (hourMinute[0]).toInt(),
                            minute = (hourMinute[1]).toInt(),
                            isMainPray = true,
                            name = "Dhuhr",
                            date = date.date
                        )
                    }

                    3 -> {
                        Time(
                            null,
                            dateId = dateId,
                            hour = (hourMinute[0]).toInt(),
                            minute = (hourMinute[1]).toInt(),
                            isMainPray = true,
                            name = "Asr",
                            date = date.date

                        )
                    }

                    5 -> {
                        Time(
                            null,
                            dateId = dateId,
                            hour = (hourMinute[0]).toInt(),
                            minute = (hourMinute[1]).toInt(),
                            isMainPray = true,
                            name = "Maghrib",
                            date = date.date

                        )
                    }

                    6 -> {
                        Time(
                            null,
                            dateId = dateId,
                            hour = (hourMinute[0]).toInt(),
                            minute = (hourMinute[1]).toInt(),
                            isMainPray = true,
                            name = "Isha",
                            date = date.date
                        )
                    }

                    else -> null
                }
                if (athanTime != null) {
                    timeDao.insert(athanTime)
                }

            }
        }
    }

    //this function to fill the _savedDate with data from room database using current day
     suspend fun getCurrentAthan(): AthanDay? {
        val currentDate = General.getCurrentDate();
        val date = dateDao.getDateByCurrentDay(currentDate);
        if (date != null) {
            val times = timeDao.getTimesByDateId(date.id!!);
            if (!times.isNullOrEmpty()) {
                val timeToAthanTime = times.map { it ->
                    AthanTime(
                        id = it.id,
                        name = it.name,
                        hour = it.hour,
                        it.minute,
                        it.isMainPray
                    )
                }
                return AthanDay(date.id, date.date, timeToAthanTime);
            }

        }
        return null;
    }

    //this i use it in check if there day athans saved
    suspend fun isThereAthanDateExist(): Boolean {
        val result = dateDao.isThereSavedDates();
        return result;
    }

    suspend fun getAthans(): List<Date>? {
      return   dateDao.getDates()
    }

    //this to check if i need to request the api again to get new Athans
     suspend fun needToRequestNewAthans(currentDate: String): Boolean {
        val athanDates = getAthans();
        if (athanDates.isNullOrEmpty()) return true
        val index = athanDates.indexOfFirst { it.date == currentDate }
        if(index==-1)return true
        return index > 4
    }


     suspend fun updateAthanAtDB(data: AthanDaysDto) {
        val days = data.days.map { it -> it.date };
        val times = data.days.map { it -> it.times };
        saveAthanDateDayAndTime(days, times)
    }


}
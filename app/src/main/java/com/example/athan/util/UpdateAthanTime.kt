package com.example.athan.util

import com.example.athan.alarm.AlarmSchedule
import com.example.athan.data.local.dao.DateDao
import com.example.athan.data.local.dao.LocationDao
import com.example.athan.data.local.dao.TimeDao
import com.example.athan.data.local.entity.Time
import com.example.athan.data.repository.ApiRepository
import com.example.athan.model.AthanTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class UpdateAthanTime @Inject constructor(
    var date: DateDao,
    var time: TimeDao,
    var location: LocationDao,
    var apiRepository: ApiRepository,
    val coroutineScope: CoroutineScope,
    val alarmSchedule: AlarmSchedule
) {
    private val _nextAthanTime = MutableStateFlow<AthanTime?>(null)
    private val _currentAthanName = MutableStateFlow<String?>(null)
    val nextAthanTime = _nextAthanTime.asStateFlow()




    fun updateNextAthanObjectFun() {

        coroutineScope.launch(Dispatchers.IO) {
            val calender = Calendar.getInstance()
            val hour = calender.get(Calendar.HOUR_OF_DAY)
            val miutes = calender.get(Calendar.MINUTE)

            //get current Day
            val currentDay = General.getCurrentDate()

            //check if the date in database or not
            getOrUpdateAthanDB(currentDay);


            //getNextAthan
            val nextAthan = getNextAthanTime(currentDay, hour)


            //update the nextathan object in general file
            updateAthanTimeObject(nextAthan, hour, miutes)

               }

    }

    suspend fun getOrUpdateAthanDB(currentDay: String) {
        val isDayIsSavedInDB = date.getDateByCurrentDay(currentDay) != null

        //to update the db data with new data
        if (!isDayIsSavedInDB) {
            val currentLocation = location.getLocation()
            if (currentLocation != null) {
                val nextDay = General.getCurrentDate(7);
                apiRepository.athanRequest(currentDay, nextDay, null, null)
            }
            return;
        }
    }

    suspend fun getCurrentDayAthan(currentDay: String): List<Time>? {

        //get current athans from db
        val currentAthanTime = time.getTimesByDate(currentDay)
        return currentAthanTime
    }

    suspend fun getNextAthanTime(currentDay: String, currentHour: Int): Time? {
        val dayAthans = getCurrentDayAthan(currentDay);
        //get the next athan time
        val nextAthan = dayAthans?.firstOrNull { it.hour >= currentHour }

        if(!dayAthans.isNullOrEmpty()&&nextAthan==null)
        {
           return dayAthans[0]
        }

        return nextAthan
    }


    suspend fun updateAthanTimeObject(nextAthan: Time?, currentHour: Int, currentMinute: Int) {
        if (nextAthan == null) {
            return;
        }

        val currentTimeToMinute = (currentHour * 60) + currentMinute
        var athanTimeToMinit = (nextAthan.hour * 60) + nextAthan.minute;
        var targetAthanTime = athanTimeToMinit - currentTimeToMinute;

        if(currentHour>18 && nextAthan.name=="الفجر")
        {
            athanTimeToMinit += abs((currentTimeToMinute) - (24 * 60))
            targetAthanTime = abs(athanTimeToMinit-currentTimeToMinute)
        }

        val targetHour = targetAthanTime / 60;
        val targetMinute = targetAthanTime % 60;

        val newAthanTime = AthanTime(
            name = nextAthan.name,
            hour = if (targetHour < 0) 0 else targetHour,
            minute = if (targetMinute < 0) 0 else targetMinute,
            isMainPray = nextAthan.isMainPray
        )

        alarmSchedule.scheduleNextAthan(nextAthan);
        withContext(Dispatchers.Main)
        {
            if (_nextAthanTime.value != newAthanTime) {
                _nextAthanTime.emit(newAthanTime)
            }
        }


    }


}
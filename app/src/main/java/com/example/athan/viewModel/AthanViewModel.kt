package com.example.athan.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.athan.data.local.dao.DateDao
import com.example.athan.data.local.dao.TimeDao
import com.example.athan.data.local.entity.Date
import com.example.athan.data.local.entity.Time
import com.example.athan.data.network.ApiCallRepository
import com.example.athan.data.network.NetworkCallHandler
import com.example.athan.data.network.dto.AthanDaysDto
import com.example.athan.model.AthanDay
import com.example.athan.model.AthanTime
import com.example.athan.util.General
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AthanViewModel @Inject constructor(
    private val apiRepository: ApiCallRepository,
    private val dateDao: DateDao,
    private val timeDao: TimeDao,
) : ViewModel() {

    private val _savedDate = MutableStateFlow<AthanDay?>(null)
    val savedDate = _savedDate.asStateFlow()

    private val _isThereSavedAthanDates = MutableStateFlow<Boolean?>(null)
    val isThereSavedAthanDates = _savedDate.asStateFlow()


    init {
        viewModelScope.launch { }
        isThereAthanDateExist()
    }

    //this i use it in my function to get the
    private suspend fun _isThereAthanDateExist(): Boolean {
        val result = dateDao.isThereSavedDates();
        _isThereSavedAthanDates.emit(result)
        return result;
    }

    //this function is use the above fun to fill the _isThereSavedAthanDates value
    fun isThereAthanDateExist() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = dateDao.isThereSavedDates();
            _isThereSavedAthanDates.emit(result)

        }
    }


    //this function to fill the _savedDate with data from room database using current day
    private suspend fun getCurrentAthan() {
        val currentDate = General.getCurrentDate();
        val date = dateDao.getDateByCurrentDay(currentDate);
        if (date != null) {
            val times = timeDao.getTimesByDateId(date.id!!);
            if (!times.isNullOrEmpty()) {
                val timeToAthanTime = times.map { it -> AthanTime(it.id, it.time, it.isMainPray) }
                _savedDate.emit(AthanDay(date.id, date.date, timeToAthanTime))
            }
        }
    }



    //this to check if i need to reqest the api again to get new Athans
    private suspend fun needToRequestNewAthans(currentDate: String): Boolean {
        val athanDates = dateDao.getDates();
        val index = athanDates?.indexOfFirst { it.date == currentDate }
        if (index != null) {
            return index > 4
        };
        return true
    }

    //this to save athan from api to room database
    private suspend fun saveAthanDateDayAndTime(days: List<String>, timeData: List<List<String>>) {
        days.forEachIndexed { index, dayData ->
            val date = Date(null, dayData);
            dateDao.insertDate(date)
            val timeData = timeData[index];
            timeData.forEachIndexed { timeIndex, time ->
                val athanTime = when (timeIndex) {
                    0 -> {
                        Time(
                            null,
                            dateId = index,
                            time = time,
                            isMainPray = true,
                            name = "الفجر"
                        )
                    }

                    1 -> {
                        Time(
                            null,
                            dateId = index,
                            time = time,
                            isMainPray = false,
                            name = "الشروق"
                        )
                    }

                    2 -> {
                        Time(
                            null,
                            dateId = index,
                            time = time,
                            isMainPray = true,
                            name = "الظهر"
                        )
                    }

                    3 -> {
                        Time(
                            null,
                            dateId = index,
                            time = time,
                            isMainPray = true,
                            name = "العصر"
                        )
                    }

                    5 -> {
                        Time(
                            null,
                            dateId = index,
                            time = time,
                            isMainPray = true,
                            name = "المغرب"
                        )
                    }

                    6 -> {
                        Time(
                            null,
                            dateId = index,
                            time = time,
                            isMainPray = true,
                            name = "العشاء"
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

    //this the api to get athan from api
    private suspend fun getAthan(
        lon: Double,
        lat: Double,
        currentDate: String,
        nextDate: String
    ): Boolean {

        val result = apiRepository.getAthanTimes(
            latitude = lat,
            longitude = lon,
            from = currentDate,
            to = nextDate
        );
        return when (result) {
            is NetworkCallHandler.Successful<*> -> {
                val data = result.data as AthanDaysDto;

                if (data.days.isNotEmpty()) {
                    val days = data.days.map { it -> it.date };
                    val times = data.days.map { it -> it.times };
                    saveAthanDateDayAndTime(days, times)
                    true

                }
                false

            }

            else -> {

                false
            }
        }
    }


    fun getAthanDates(long: Double, lat: Double) {
        viewModelScope.launch(Dispatchers.IO) {

            val isThereSavedDates = _isThereAthanDateExist()
            when (isThereSavedDates) {
                true -> {
                    val currentDate = General.getCurrentDate();
                    val isNeedToRequestNewAthanDates = needToRequestNewAthans(currentDate)
                    if (isNeedToRequestNewAthanDates) {
                        val nextDate = General.getCurrentDate(6)
                        val result = getAthan(long, lat, currentDate, nextDate)
                        if (result) {
                            getCurrentAthan()
                        }
                    }
                }

                else -> {

                }
            }
        }
    }


}
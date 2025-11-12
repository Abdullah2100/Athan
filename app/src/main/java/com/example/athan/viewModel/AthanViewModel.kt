package com.example.athan.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.athan.data.local.dao.LocationDao
import com.example.athan.data.local.entity.Location
import com.example.athan.data.repository.ApiRepository
import com.example.athan.data.repository.LocalDBRepository
import com.example.athan.model.AthanDay
import com.example.athan.util.General
import com.example.athan.util.UpdateAthanTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltViewModel
class AthanViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val locationDao: LocationDao,
    private val localDBRepository: LocalDBRepository,
    private val athanUpdateOb: UpdateAthanTime,
    private val appScope:CoroutineScope

    ) : ViewModel() {

    //private
    val _athanDay = MutableStateFlow<AthanDay?>(null)

     val athanDay = _athanDay.asStateFlow()


    private val _isThereSavedAthanDates = MutableStateFlow<Boolean?>(null)
    val isThereSavedAthanDates = _isThereSavedAthanDates.asStateFlow()

    private val _isSavedUserLocation = MutableStateFlow<Boolean?>(null)
    val isSavedUserLocation = _isSavedUserLocation.asStateFlow()

    val currentAthanOb = athanUpdateOb.nextAthanTime

    init {
        isUserHasSavedLocation()
        isThereAthanDateExist()
    }


    //this to update object of _isSavedUserLocaiton
    private fun isUserHasSavedLocation() {
        appScope.launch(Dispatchers.IO) {
            val isThereSavedLocation = locationDao.getLocation() != null;
            _isSavedUserLocation.emit(isThereSavedLocation);
        }
    }


    //this to save user location to local db
    fun saveUserLocationToDB(lat: Double, long: Double) {
        appScope.launch(Dispatchers.IO) {
            locationDao.insert(Location(0, long, lat))
        }
    }


    //this function is use the above fun to fill the _isThereSavedAthanDates value
    fun isThereAthanDateExist() {
        appScope.launch(Dispatchers.IO) {
            val result = localDBRepository.isThereAthanDateExist();
            _isThereSavedAthanDates.emit(result)
        }
    }


    //this function to fill the _savedDate with data from room database using current day
    private suspend fun updateAthanObject() {
            _athanDay.emit(localDBRepository.getCurrentAthan())
    }


    //get Athan from api
    fun getAthanDates(lat: Double? = null, lon: Double? = null) {
        appScope.launch(Dispatchers.IO) {

            val isThereSavedDates = localDBRepository.isThereAthanDateExist()

            when (isThereSavedDates) {
                false -> {
                    val currentDate = General.getCurrentDate();
                    val isNeedToRequestNewAthanDates =
                        localDBRepository.needToRequestNewAthans(currentDate)
                    if (isNeedToRequestNewAthanDates) {
                        val nextDate = General.getCurrentDate(6)
                        apiRepository.athanRequest(
                            currentDate,
                            nextDate,
                            lat,
                            lon,
                        )
                        athanUpdateOb.updateNextAthanObjectFun()
                        updateAthanObject()

                    }
                }

                else -> {
                    athanUpdateOb.updateNextAthanObjectFun()
                    updateAthanObject()
                }
            }
        }
    }


}
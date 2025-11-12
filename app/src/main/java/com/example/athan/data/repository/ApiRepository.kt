package com.example.athan.data.repository

import com.example.athan.data.local.dao.LocationDao
import com.example.athan.data.network.NetworkCallHandler
import com.example.athan.data.network.dto.AthanDaysDto
import com.example.athan.util.General
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class ApiRepository @Inject constructor(
    val ktorClient: HttpClient,
    val location: LocationDao,
    val localDBRepository: LocalDBRepository
) {


    private suspend fun getAthans(
        longitude: Double,
        latitude: Double,
        from: String,
        to: String
    ): NetworkCallHandler {
        return try {
            val baseUrl = General.bastUr(
                longitude = longitude,
                latitude = latitude,
                from = from,
                to = to
            );
            val result = ktorClient.get(baseUrl)

            if (result.status == HttpStatusCode.Companion.OK) {
                return NetworkCallHandler.Successful(result.body<AthanDaysDto>())
            } else {
                NetworkCallHandler.Error(result.body<String>())
            }

        } catch (e: UnknownHostException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: IOException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: Exception) {

            return NetworkCallHandler.Error(e.message)
        }
    }

    private suspend fun getAthans(
        from: String,
        to: String
    ): NetworkCallHandler {
        val savedLocation = location.getLocation()
        if (savedLocation == null) return NetworkCallHandler.Error("Location is Not ")
        return try {
            val baseUrl = General.bastUr(
                longitude = savedLocation.log,
                latitude = savedLocation.lat,
                from = from,
                to = to
            );
            val result = ktorClient.get(baseUrl)

            if (result.status == HttpStatusCode.Companion.OK) {
                return NetworkCallHandler.Successful(result.body<AthanDaysDto>())
            } else {
                NetworkCallHandler.Error(result.body<String>())
            }

        } catch (e: UnknownHostException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: IOException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: Exception) {

            return NetworkCallHandler.Error(e.message)
        }
    }


    suspend fun athanRequest(
        currentDate: String,
        nextDate: String,
        lat: Double?,
        lon: Double?
    ) {
        val result = if (lat == null && lon == null) getAthans(
            currentDate,
            nextDate,
        )
        else
            getAthans(

                lat!!,
                lon!!,
                currentDate,
                nextDate,
            )
        when (result) {
            is NetworkCallHandler.Successful<*> -> {
                val data = result.data as AthanDaysDto;

                localDBRepository.updateAthanAtDB(data)
                data
            }

            else -> {

                null
            }
        }
    }


}
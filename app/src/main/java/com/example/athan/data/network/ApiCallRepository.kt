package com.example.athan.data.network

import com.example.athan.data.network.dto.AthanDto
import com.example.athan.util.General
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class ApiCallRepository @Inject constructor(val ktorClient: HttpClient) {

    suspend fun getAthanTimes(
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

            if (result.status == HttpStatusCode.OK) {
                return NetworkCallHandler.Successful(result.body<AthanDto>())
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


}
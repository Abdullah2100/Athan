package com.example.athan.data.network

sealed class NetworkCallHandler() {
    data class Successful<out T>(val data: T) : NetworkCallHandler()
    data class Error(val data: String?) : NetworkCallHandler()
}
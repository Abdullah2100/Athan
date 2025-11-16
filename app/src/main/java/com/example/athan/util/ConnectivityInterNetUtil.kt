package com.example.athan.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class ConnectivityInterNetUtil @Inject constructor(@ApplicationContext private  val context: Context){

    val isConnectToInternet = MutableStateFlow<Boolean>(false)
private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager

    init {
      connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
          override fun onAvailable(network: Network) {
              isConnectToInternet.value=true
              super.onAvailable(network)
          }

          override fun onLost(network: Network) {
              isConnectToInternet.value=false
              super.onLost(network)
          }
      })
    }


}
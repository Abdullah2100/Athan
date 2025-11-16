package com.example.athan.viewModel

import androidx.lifecycle.ViewModel
import com.example.athan.util.ConnectivityInterNetUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(private val connectivityInterNetUtil: ConnectivityInterNetUtil) :
    ViewModel() {

    private val _isConnectToInternet = connectivityInterNetUtil.isConnectToInternet
    val isConnectToInternet = _isConnectToInternet.asStateFlow()

}
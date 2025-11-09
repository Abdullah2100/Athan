package com.example.athan.viewModel

import androidx.lifecycle.ViewModel
import com.example.athan.data.network.ApiCallRepository
import jakarta.inject.Inject

class AthanViewModel @Inject constructor(
    val apiRepository: ApiCallRepository,
) : ViewModel() {

    init {

    }



}
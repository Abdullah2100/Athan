package com.example.athan.data.network.dto

import com.example.athan.model.AthanTime
import kotlinx.serialization.Serializable

@Serializable
data class AthanTimeDto(
    val date:String,
    val times:List<String>,
)
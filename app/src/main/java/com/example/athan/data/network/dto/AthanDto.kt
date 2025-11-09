package com.example.athan.data.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class AthanDto(
    val days:List<AthanDayDto>
)

@Serializable
data class AthanDayDto(
    val date:String,
    val times:List<String>,
)
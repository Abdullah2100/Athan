package com.example.athan.data.network.dto

import com.example.athan.model.AthanDay
import kotlinx.serialization.Serializable

@Serializable
data class AthanDaysDto(
    val days:List<AthanTimeDto>
)


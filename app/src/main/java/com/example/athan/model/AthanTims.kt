package com.example.athan.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class AthanTime(
     val id: Int? = null,
     val name:String,
     val hour:Int,
     val minute:Int,
    val isMainPray:Boolean,
)

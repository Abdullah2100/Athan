package com.example.athan.model

import androidx.room.PrimaryKey

data class AthanTime(
     val id: Int? = null,
    val time:String,
    val isMainPray:Boolean,
)

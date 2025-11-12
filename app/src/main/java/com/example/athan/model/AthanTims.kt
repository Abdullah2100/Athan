package com.example.athan.model

import androidx.room.PrimaryKey

data class AthanTime(
     val id: Int? = null,
     val name:String,
     val hour:Int,
     val minute:Int,
    val isMainPray:Boolean,
)

package com.example.athan.model

import com.example.athan.data.local.entity.Time

data class AthanDay(
    var id:Int,
    val date:String,
    val athanTimes: List<AthanTime>
)

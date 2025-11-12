package com.example.athan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("Location")
data class Location(
    @PrimaryKey val id:Int,
    val log: Double,
    val lat: Double
)

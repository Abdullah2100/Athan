package com.example.athan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("AthanDate")
data class Date(
    @PrimaryKey val id: Int? = null,
    val date: String,
)

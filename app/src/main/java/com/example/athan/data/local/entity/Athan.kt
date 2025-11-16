package com.example.athan.data.local.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Athan")
data class Athan(
    @PrimaryKey()
    val id: Int = 0,
    val path: String
)
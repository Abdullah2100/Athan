package com.example.athan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Language")
data class Language(
    @PrimaryKey val id:Int=0,
    val name:String
)

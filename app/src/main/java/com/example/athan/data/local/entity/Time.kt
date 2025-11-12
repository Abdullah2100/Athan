package com.example.athan.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity("AthanTime",
    foreignKeys = [
        ForeignKey(
            entity = Date::class,
            parentColumns = ["id"],
            childColumns = ["dateId"],
            onDelete = CASCADE
        )
    ]
)
data class Time(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val hour:Int,
    val minute:Int,
    val name:String,
    val isMainPray:Boolean,
    val dateId:Int,
    val date:String,
)

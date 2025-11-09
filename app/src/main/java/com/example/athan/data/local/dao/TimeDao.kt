package com.example.athan.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.athan.data.local.entity.Date
import com.example.athan.data.local.entity.Time

interface TimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTime(time: Time)

    @Query("SELECT * FROM date")
    suspend fun getTimes(date: String): List<Date>?
}
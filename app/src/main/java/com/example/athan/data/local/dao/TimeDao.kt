package com.example.athan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.athan.data.local.entity.Date
import com.example.athan.data.local.entity.Time

@Dao
interface TimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(time: Time)

    @Query("SELECT * FROM AthanTime")
    suspend fun getTimes(): List<Time>

    @Query("SELECT * FROM AthanTime where dateId=:dateId")
    suspend fun getTimesByDateId(dateId:Int): List<Time>


    @Query("SELECT * FROM AthanTime where date=:date")
    suspend fun getTimesByDate(date: String): List<Time>
}
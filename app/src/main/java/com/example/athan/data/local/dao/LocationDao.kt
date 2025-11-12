package com.example.athan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.athan.data.local.entity.Location
import com.example.athan.data.local.entity.Time

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: Location)

    @Query("SELECT * FROM Location where id=0")
    suspend fun getLocation(): Location?
}
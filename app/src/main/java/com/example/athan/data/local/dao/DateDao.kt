package com.example.athan.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.athan.data.local.entity.Date

@Dao
interface DateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDate(date: Date)

    @Query("SELECT * FROM date")
    suspend fun getDateByDate(date: String): List<Date>?

    @Query("delete from date where id<:id")
    suspend fun deleteDate(id: Int)

}
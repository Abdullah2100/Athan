package com.example.athan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.athan.data.local.entity.Date

@Dao
interface DateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDate(date: Date)

    @Query("SELECT * FROM AthanDate")
    suspend fun getDates(): List<Date>

    @Query("SELECT * FROM AthanDate WHERE date = :selectedDate")
    suspend fun getDateByCurrentDay(selectedDate: String): Date?

    @Query("SELECT COUNT(*)>0 FROM AthanDate")
    suspend fun isThereSavedDates(): Boolean

    @Query("delete from AthanDate WHERE id<:id")
    suspend fun deleteDate(id: Int)

}
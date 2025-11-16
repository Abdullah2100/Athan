package com.example.athan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.athan.data.local.entity.Athan

@Dao
interface AthanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthan(athan: Athan)

    @Query("SELECT * FROM Athan where id =1")
    suspend fun getAthan(): Athan?


}
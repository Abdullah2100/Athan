package com.example.athan.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.athan.data.local.entity.Language
import kotlinx.coroutines.flow.Flow

@Dao
interface LocaleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(language: Language)

    @Query("SELECT * FROM Language WHERE id=0")
     fun getSavedLanguage(): Flow<Language?>


    @Query("INSERT INTO Language (id, name) SELECT 0, :name WHERE NOT EXISTS (SELECT * FROM Language WHERE id = 0)")
    suspend fun setDefaultLocale(name:String)
}

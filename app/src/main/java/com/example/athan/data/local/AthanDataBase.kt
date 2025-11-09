package com.example.athan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.athan.data.local.dao.DateDao
import com.example.athan.data.local.dao.TimeDao
import com.example.athan.data.local.entity.Date
import com.example.athan.data.local.entity.Time

@Database(
    entities = [
        Date::class,
        Time::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AthanDataBase : RoomDatabase() {
    abstract fun dateDao(): DateDao
    abstract fun timeDao(): TimeDao
}
package com.example.athan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.athan.data.local.dao.AthanDao
import com.example.athan.data.local.dao.DateDao
import com.example.athan.data.local.dao.LocationDao
import com.example.athan.data.local.dao.TimeDao
import com.example.athan.data.local.entity.Athan
import com.example.athan.data.local.entity.Date
import com.example.athan.data.local.entity.Location
import com.example.athan.data.local.entity.Time

@Database(
    entities = [
        Date::class,
        Time::class,
        Location::class,
        Athan::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AthanDataBase : RoomDatabase() {
    abstract fun dateDao(): DateDao
    abstract fun timeDao(): TimeDao
    abstract fun locationDao(): LocationDao
    abstract fun athanDao(): AthanDao

}
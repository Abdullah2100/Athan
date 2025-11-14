package com.example.athan.di

import android.app.AlarmManager
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.athan.data.local.AthanDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json


@Module
@InstallIn(SingletonComponent::class)
object Model {

    @Provides
    fun httpClient(): HttpClient {
        return HttpClient(Android) {

            engine {
                connectTimeout = 60_000
            }

            install(WebSockets)

            install(HttpTimeout)

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Logger Ktor =>", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Provides
    fun generalContext(@ApplicationContext context: Context): Context {
        return context;
    }

    @Singleton
    @Provides
    fun alermSetting(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Provides
    @Singleton
    fun athanDB(context: Context): AthanDataBase {
        return Room.databaseBuilder(
            context,
            AthanDataBase::class.java, "authDB.db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }


    @Provides
    @Singleton
    fun dateDao(athanDataBase: AthanDataBase) = athanDataBase.dateDao()

    @Provides
    @Singleton
    fun timeDao(athanDataBase: AthanDataBase) = athanDataBase.timeDao()

    @Provides
    @Singleton
    fun locationDao(athanDataBase: AthanDataBase) = athanDataBase.locationDao()


    @Provides
    @Singleton
    fun applicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob()+ Dispatchers.Default)
    }
}
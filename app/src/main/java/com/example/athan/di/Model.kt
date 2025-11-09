package com.example.athan.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.athan.data.local.AthanDataBase
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import org.junit.runner.manipulation.Ordering


@InstallIn(SingletonComponent::class)
class Model {

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

}
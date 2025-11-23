package com.example.athan.data.repository

import com.example.athan.data.local.dao.LocaleDao
import com.example.athan.data.local.entity.Language
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocaleRepository @Inject constructor(
    private val localeDao: LocaleDao
) {

    suspend fun saveLanguage(language: Language) {
        localeDao.insert(language)
    }

    suspend fun setDefaultLocale(name: String) {
        localeDao.setDefaultLocale(name)
    }

     fun getSavedLanguage(): Flow<Language?> {
        return localeDao.getSavedLanguage()
    }


}
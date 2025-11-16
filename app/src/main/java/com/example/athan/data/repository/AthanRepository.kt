package com.example.athan.data.repository

import com.example.athan.data.local.dao.AthanDao
import com.example.athan.data.local.entity.Athan
import javax.inject.Inject

class AthanRepository @Inject constructor( private val athanDao: AthanDao) {

    suspend fun  getSavedAthan(): Athan?{
        val athan = athanDao.getAthan()
        return athan
    }

   suspend fun savedAthan(athan: Athan){
        athanDao.insertAthan(athan)
    }
}
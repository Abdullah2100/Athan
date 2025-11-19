package com.example.athan.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.athan.util.UpdateAthanTime
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateAthanWorder @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    val updateAthanTim: UpdateAthanTime
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        try {

            updateAthanTim.updateNextAthanObjectFun()
            return Result.success()
        }
        catch (e: Exception)
        {
            Log.d("thisErrorFromWorker",e.stackTrace.toString())
            return Result.failure()
        }
    }

}
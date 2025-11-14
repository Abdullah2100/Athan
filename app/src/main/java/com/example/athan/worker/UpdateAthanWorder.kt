package com.example.athan.worker

import android.content.Context
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
        updateAthanTim.updateNextAthanObjectFun()
        return Result.success()
    }
}
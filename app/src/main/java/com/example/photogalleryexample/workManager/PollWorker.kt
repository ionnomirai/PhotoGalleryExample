package com.example.photogalleryexample.workManager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

private const val TAG = "PollWorker_TAG"

class PollWorker (
    private val context: Context,
    workerParameters: WorkerParameters
        ) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "Work request triggered")
        return Result.success()
    }
}
package com.example.skywise.alertscreen

import android.content.Context
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class OneTimeWorker(private val appContext: Context, params: WorkerParameters) :CoroutineWorker(appContext,
    params
) {
    override suspend fun doWork(): Result {
        val description = inputData.getString("description")!!
        makeAlert(description)
        return Result.success()
    }

    private fun makeAlert(description: String) {
        val intent = Intent(applicationContext, AlertService::class.java)
        intent.putExtra("Stringayte", description)
        ContextCompat.startForegroundService(applicationContext, intent)
    }
}
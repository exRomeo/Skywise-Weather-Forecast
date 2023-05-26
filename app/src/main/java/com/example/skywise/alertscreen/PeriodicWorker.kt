package com.example.skywise.alertscreen

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.skywise.data.Repository
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit

class PeriodicWorker(val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    val repository by lazy { Repository(RetrofitClient, RoomClient.getInstance(context)) }


    override suspend fun doWork(): Result {
        if (!isStopped) {
            val id = inputData.getLong("id", 0)
            getData(id.toInt())
        }
        return Result.success()
    }


    private suspend fun getData(id: Int) {
        val currentWeather = repository.getPeriodic()
        val alert = repository.getAlertByID(id)
        if (alert.startDate!! > (currentTimeMillis())) {
            val delay: Long = alert.startDate!! - currentTimeMillis()
            if (currentWeather.alerts.isNullOrEmpty()) {
                runOneTimeWorker(
                    delay,
                    alert.id,
                    currentWeather.current.weather[0].description!!
                )

            } else {
                runOneTimeWorker(
                    delay,
                    alert.id,
                    currentWeather.alerts[0].tags?.get(0)!!
                )

            }
        } else {
            repository.removeAlert(alert)
            WorkManager.getInstance(applicationContext).cancelAllWorkByTag("$id")
        }
    }


    private fun runOneTimeWorker(delay: Long, id: Int, description: String) {

        val data = Data.Builder().putString("description", description).build()

        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true).build()
        val oneTimeWorkRequest =
            OneTimeWorkRequest.Builder(OneTimeWorker::class.java)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .setInputData(data)
                .build()
        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )

    }
}
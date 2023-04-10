package com.example.skywise.alertscreen

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.example.skywise.data.Repository
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.utils.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withContext
import com.example.skywise.R
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
   /* @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        val data = withContext(Dispatchers.IO) {
            repository.getPeriodic()
        }

        // Build the notification
        val notification = NotificationUtils.makeNotification(
            applicationContext,
            R.drawable.skywise_dark, data.area,
            "${data.current.weather[0].description}\n${applicationContext.getString(R.string.min)} ${
                data.daily?.get(
                    0
                )?.temp?.getMinText()
            } ${applicationContext.getString(R.string.max)} ${
                data.daily?.get(
                    0
                )?.temp?.getMaxText()
            }", NotificationCompat.PRIORITY_HIGH
        )

        // Send the notification
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(
            0, notification
        )

        // Indicate that the work was successful
        return Result.success()
    }*/
}
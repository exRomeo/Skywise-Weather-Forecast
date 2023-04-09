package com.example.skywise.alertscreen

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.skywise.R
import com.example.skywise.data.Repository
import com.example.skywise.data.WeatherData
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.utils.NotificationUtils

class NotificationAlertWorker(
    private val context: Context, workerParameters: WorkerParameters,

    ) : CoroutineWorker(context, workerParameters) {

    private val repository by lazy { Repository(RetrofitClient, RoomClient.getInstance(context)) }

    override suspend fun doWork(): Result {
        val id = inputData.getString("id")!!.toInt()
        val data = repository.getPeriodic()
        val notification = NotificationUtils.makeNotification(
            context, R.drawable.skywise_dark, data.getCurrentLocation(), notificationText(data)
        )

        NotificationUtils.notificationManager.notify(
            0, notification
        )
        repository.removeAlertByID(id)

        return Result.success()
    }


    private fun notificationText(weatherData: WeatherData): String {
        return if (weatherData.alerts.isNullOrEmpty()) weatherData.current.weather[0].description!! + " ${
            context.getString(
                R.string.max
            )
        } " + weatherData.daily!![0].temp!!.getMaxText() + " ${
            context.getString(
                R.string.min
            )
        } " + weatherData.daily[0].temp!!.getMinText()
        else weatherData.alerts[0].tags!![0] + " " + weatherData.alerts[0].description
    }
}
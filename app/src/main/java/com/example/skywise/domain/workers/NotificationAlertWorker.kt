package com.example.skywise.domain.workers

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.skywise.R
import com.example.skywise.data.apiservices.WeatherApi
import com.example.skywise.data.localsource.LocalSourceImpl
import com.example.skywise.data.models.WeatherData
import com.example.skywise.data.remotesource.RemoteSourceImpl
import com.example.skywise.data.repository.RepositoryImpl
import com.example.skywise.data.roomdb.RoomClient
import com.example.skywise.domain.constants.APIKEY
import com.example.skywise.domain.constants.BASE_URL
import com.example.skywise.domain.utils.NotificationUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationAlertWorker(
    context: Context, workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    private val repositoryImpl by lazy {
        RepositoryImpl(
            RemoteSourceImpl(
                apiKey = APIKEY, weatherApi = Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(
                        WeatherApi::class.java
                    )
            ),
            LocalSourceImpl(
                Room.databaseBuilder(
                    applicationContext,
                    RoomClient::class.java,
                    "skywise_database"
                ).build().getWeatherDatabase()
            )
        )
    }

    override suspend fun doWork(): Result {
        val id = inputData.getString("id")!!.toInt()
        val data = repositoryImpl.getPeriodic()
        val notification = NotificationUtils.makeNotification(
            applicationContext,
            R.drawable.skywise_dark,
            data.getCurrentLocation(),
            notificationText(data)
        )

        NotificationUtils.notificationManager.notify(
            0, notification
        )
        repositoryImpl.removeAlertByID(id)

        return Result.success()
    }


    private fun notificationText(weatherData: WeatherData): String {
        return if (weatherData.alerts.isNullOrEmpty()) weatherData.current.weather[0].description!! + " ${
            applicationContext.getString(
                R.string.max
            )
        } " + weatherData.daily!![0].temp!!.getMaxText() + " ${
            applicationContext.getString(
                R.string.min
            )
        } " + weatherData.daily[0].temp!!.getMinText()
        else weatherData.alerts[0].tags!![0] + " " + weatherData.alerts[0].description
    }
}
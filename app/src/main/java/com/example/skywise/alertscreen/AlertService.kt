package com.example.skywise.alertscreen

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.skywise.R
import com.example.skywise.data.Current
import com.example.skywise.data.Weather
import com.example.skywise.data.WeatherData
import com.example.skywise.settingsscreen.SkywiseSettings
import com.example.skywise.settingsscreen.SkywiseSettings.ALERT
import com.example.skywise.utils.NotificationUtils

class AlertService : Service() {

    var alertWindowManager: AlertWindowManager? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val data = intent?.getStringExtra("Stringayte") ?: "N/A"

        this.startForeground(
            187,
            NotificationUtils.makeNotification(
                this,
                R.drawable.skywise_dark,
                data,
                data, NotificationCompat.PRIORITY_DEFAULT
            )
        )

        if (SkywiseSettings.alertType == ALERT) {
            alertWindowManager = AlertWindowManager(
                this,
                WeatherData(current = Current(weather = arrayListOf(Weather(description = data))))
            )
            alertWindowManager?.initialize()

        }
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}


/*    lateinit var weatherData: Flow<WeatherData>

    //    var AlertWindowManager: AlertWindowManager? = null
    val repository by lazy { Repository(RetrofitClient, RoomClient.getInstance(this)) }
    override fun onCreate() {
        super.onCreate()
        weatherData = repository.getLocationData(
            lat, lon, lang, units, listOf(
                HOURLY,
                MINUTELY
            ), API_KEY
        )

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        weatherData.onEach {
            startForeground(
                4,
                NotificationUtils.makeNotification(
                    this,
                    R.drawable.skywise_dark,
                    it.area,
                    it.current.weather[0].description!! + " " + this.getString(R.string.max) + " " + (it.daily?.get(
                        0
                    )?.temp?.getMaxText()
                        ?: "N/A") + this.getString(R.string.min) + " " + (it.daily?.get(0)?.temp?.getMinText()
                        ?: "N/A"),
                )
            )
            if (SkywiseSettings.alertType == ALERT) {
                alertWindowManager = AlertWindowManager(
                    this,
                    it
                )
                alertWindowManager?.initialize()

            }
        }.launchIn(CoroutineScope(Dispatchers.Main))


        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}*/

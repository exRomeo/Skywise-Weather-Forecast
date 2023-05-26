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

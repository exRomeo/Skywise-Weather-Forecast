package com.example.skywise.alertscreen

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.*
import androidx.databinding.DataBindingUtil
import com.example.skywise.R
import com.example.skywise.data.WeatherData
import com.example.skywise.databinding.WeatherAlertDialogLayoutBinding

class AlertWindowManager(private val context: Context, private val weatherData: WeatherData) {
    private lateinit var windowManager: WindowManager
    private lateinit var binding: WeatherAlertDialogLayoutBinding

    fun initialize() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.weather_alert_dialog_layout,
            null,
            false
        )
        binding.weatherData = weatherData
        binding.button.setOnClickListener {
            close()
            endService()
        }
        windowManager = context.getSystemService(WindowManager::class.java)
        val parameters = WindowManager.LayoutParams(
            (context.resources.displayMetrics.widthPixels * 0.9).toInt(),
            WRAP_CONTENT,
            TYPE_APPLICATION_OVERLAY,
            FLAG_KEEP_SCREEN_ON or FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        windowManager.addView(binding.root, parameters)
    }

    fun close() {
        windowManager.removeView(binding.root)
        binding.root.invalidate()
    }

    fun endService() {
        context.stopService(Intent(context, AlertService::class.java))
    }
}
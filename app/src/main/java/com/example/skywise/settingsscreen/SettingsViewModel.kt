package com.example.skywise.settingsscreen

import androidx.lifecycle.ViewModel
import com.example.skywise.IMPERIAL
import com.example.skywise.METRIC
import com.example.skywise.STANDARD

class SettingsViewModel : ViewModel() {


    fun setMetric() {
        SkywiseSettings.setUnits(METRIC)
    }
    fun setStandard() {
        SkywiseSettings.setUnits(STANDARD)
    }
    fun setImperial() {
        SkywiseSettings.setUnits(IMPERIAL)
    }
}
package com.example.skywise.settingsscreen

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.skywise.mapsheet.MapSheet


class SettingsViewModel : ViewModel() {

    fun setUnits(units: String) {
        SkywiseSettings.setUnits(units)
    }

    fun setLanguage(language: String, activity: Activity) {
        SkywiseSettings.setLang(language)
        SkywiseSettings.updateLocale(activity)
        activity.recreate()
    }

    fun setLocationType(locationType: String) {
        SkywiseSettings.setLocationType(locationType)
    }

    fun setAlertType(alertType: String) {
        SkywiseSettings.setAlertType(alertType)
    }


    fun showMap(supportFragmentManager: FragmentManager) {
        MapSheet().show(
            supportFragmentManager,
            "mapFragment"
        )
        SkywiseSettings.requiresLocation = true
    }

}


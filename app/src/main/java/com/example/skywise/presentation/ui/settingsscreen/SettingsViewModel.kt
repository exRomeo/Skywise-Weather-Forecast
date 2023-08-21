package com.example.skywise.presentation.ui.settingsscreen

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.skywise.domain.settings.SkywiseSettings
import com.example.skywise.presentation.ui.mapsheet.MapSheet
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel  @Inject constructor(): ViewModel() {

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


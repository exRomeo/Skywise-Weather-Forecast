package com.example.skywise.favoritelocations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywise.data.FavoriteLocation
import com.example.skywise.data.Repository
import com.example.skywise.data.WeatherData
import com.example.skywise.data.remotesource.API_KEY
import com.example.skywise.settingsscreen.SkywiseSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteLocationsViewModel(val repository: Repository) : ViewModel() {

    private var _favoriteLocations = MutableStateFlow(listOf(FavoriteLocation(0.0, 0.0)))
    val favoriteLocation = _favoriteLocations.asStateFlow()

    private var _shownLocation = MutableStateFlow(WeatherData())
    val shownLocation = _shownLocation.asStateFlow()
    fun getFavoriteLocations() {
        viewModelScope.launch {
            repository.getFavoriteLocations().collect {
                _favoriteLocations.value = it
            }
        }
    }

    fun getLocationData(favoriteLocation: FavoriteLocation) {
        viewModelScope.launch {
            _shownLocation.value = WeatherData()
            repository.getLocationData(
                favoriteLocation.lat,
                favoriteLocation.lon,
                SkywiseSettings.lang,
                SkywiseSettings.units,
                listOf(
                    SkywiseSettings.MINUTELY,
                    SkywiseSettings.HOURLY
                ),
                API_KEY
            ).collect {
                _shownLocation.value = it
            }
        }
    }

    fun removeLocation(location: FavoriteLocation) {
        viewModelScope.launch {
            repository.removeLocation(location)
        }
    }
}
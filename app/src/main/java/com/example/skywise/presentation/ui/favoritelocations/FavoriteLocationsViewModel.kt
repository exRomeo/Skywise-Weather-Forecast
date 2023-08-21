package com.example.skywise.presentation.ui.favoritelocations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywise.data.models.FavoriteLocation
import com.example.skywise.data.models.WeatherData
import com.example.skywise.data.repository.Repository
import com.example.skywise.domain.settings.SkywiseSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteLocationsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

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
                "${SkywiseSettings.MINUTELY},${SkywiseSettings.HOURLY}"
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
package com.example.skywise.favoritelocations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywise.data.FavoriteLocation
import com.example.skywise.data.Repository
import com.example.skywise.data.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteLocationsViewModel(val repository: Repository) : ViewModel() {

    private var _favoriteLocations = MutableStateFlow(listOf(FavoriteLocation(55.0,55.0)))
    val favoriteLocation = _favoriteLocations.asStateFlow()

    private var _shownLocation = MutableStateFlow(WeatherData())
    val shownLocation = _shownLocation.asStateFlow()
    fun getFavoriteLocations(){
        viewModelScope.launch {
            repository.getFavoriteLocations().collect(){
                _favoriteLocations.value = it
            }
        }
    }
    fun getLocationData(favoriteLocation: FavoriteLocation){
        viewModelScope.launch{
            repository.getLatestData(favoriteLocation.lat, favoriteLocation.lon).collect() {
                _shownLocation.value = it
            }
        }
    }

    fun removeLocation(location: FavoriteLocation){
        viewModelScope.launch {
            repository.removeLocation(location) }
    }
}
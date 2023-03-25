package com.example.skywise.weatherscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywise.data.Repository
import com.example.skywise.data.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: Repository) : ViewModel() {

    private val _weatherData: MutableStateFlow<WeatherData> = MutableStateFlow(WeatherData())
    val weatherData = _weatherData.asSharedFlow()

    private val _snackBarText = MutableSharedFlow<String>()
    val snackBarText = _snackBarText.asSharedFlow()

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            _weatherData.value = repository.getLatestData()

            Log.i("TAG", "updateData: updated")
        }
    }

    fun update() {
        updateData()
        viewModelScope.launch {
            _snackBarText.emit("Data Updated")
        }
    }

}
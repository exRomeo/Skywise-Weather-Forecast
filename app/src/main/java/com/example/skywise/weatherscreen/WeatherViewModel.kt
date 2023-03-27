package com.example.skywise.weatherscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywise.data.Repository
import com.example.skywise.data.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: Repository) : ViewModel() {

    private val _weatherData: MutableStateFlow<WeatherData> = MutableStateFlow(WeatherData())
    val weatherData = _weatherData.asStateFlow()

    private val _snackBarText = MutableSharedFlow<String>()
    val snackBarText = _snackBarText.asSharedFlow()

    fun updateData() {
        Log.i("TAG", "updateData: updating...")
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.readSavedLocation()/*getLatestData()*/
            Log.i("TAG", "updateData: -????????" + response.isSuccessful)
            if (response.isSuccessful && response.body() != null) {
                _weatherData.value = response.body() as WeatherData
                Log.i("TAG", "updateData: updated")
            } else{
                _snackBarText.emit("Couldn't retrieve data")
            }
        }
    }

    fun update() {
        updateData()
        viewModelScope.launch {
            Log.i("TAG", " clicked")
            _snackBarText.emit("Data Updated")
        }
    }

}
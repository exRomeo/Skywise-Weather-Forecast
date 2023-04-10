package com.example.skywise.weatherscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skywise.data.Repository

class WeatherViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) WeatherViewModel(
            repository
        ) as T else throw IllegalArgumentException("View Model Class Not Found!!!")
    }
}
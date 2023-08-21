package com.example.skywise.presentation.state

import com.example.skywise.data.models.WeatherData

sealed class UIState {

    class SuccessOnline(val weatherData: WeatherData) : UIState()

    class SuccessOffline(val weatherData: WeatherData) : UIState()

    class Failure(val throwable: Throwable) : UIState()

    object Loading : UIState()
}



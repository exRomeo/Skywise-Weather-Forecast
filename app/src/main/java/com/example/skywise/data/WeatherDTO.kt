package com.example.skywise.data

import com.example.skywise.data.localsource.OfflineDataModel

sealed class WeatherDTO {

    class SuccessOnline(val weatherData: WeatherData) : WeatherDTO()

    class SuccessOffline(val offlineDataModel: OfflineDataModel) : WeatherDTO()

    class Failure(val throwable: Throwable) : WeatherDTO()

    object Loading : WeatherDTO()
}



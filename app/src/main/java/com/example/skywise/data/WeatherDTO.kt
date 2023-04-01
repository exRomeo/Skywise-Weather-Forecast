package com.example.skywise.data

sealed class WeatherDTO {

    class SuccessOnline(val weatherData: WeatherData) : WeatherDTO()

    class SuccessOffline(val weatherData: WeatherData) : WeatherDTO()

    class Failure(val throwable: Throwable) : WeatherDTO()

    object Loading : WeatherDTO()
}



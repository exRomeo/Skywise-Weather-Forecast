package com.example.skywise.utils

import com.example.skywise.data.Current
import com.example.skywise.data.Weather
import com.example.skywise.data.WeatherData
import com.example.skywise.data.localsource.OfflineDataModel

object DataUtils {
    fun onlineDataToOfflineData(weatherData: WeatherData): OfflineDataModel {
        return OfflineDataModel(
            lat = weatherData.lat,
            lon = weatherData.lon,
            timezone_offset = weatherData.timezone_offset,
            dt = weatherData.current.dt,
            sunrise = weatherData.current.sunrise,
            sunset = weatherData.current.sunset,
            temp = weatherData.current.temp,
            feels_like = weatherData.current.feels_like,
            pressure = weatherData.current.pressure,
            humidity = weatherData.current.humidity,
            dew_point = weatherData.current.dew_point,
            uvi = weatherData.current.uvi,
            clouds = weatherData.current.clouds,
            visibility = weatherData.current.visibility,
            wind_speed = weatherData.current.wind_speed,
            wind_deg = weatherData.current.wind_deg,
            wind_gust = weatherData.current.wind_gust,
            description = weatherData.current.weather[0].description,
            icon = weatherData.current.weather[0].icon
            , area = weatherData.area
        )
    }

    fun offlineDataToOnlineData(offlineDataModel: OfflineDataModel): WeatherData {
        val weatherData =  WeatherData(
            lat = offlineDataModel.lat,
            lon = offlineDataModel.lon,
            timezone_offset = offlineDataModel.timezone_offset,
            current = Current(
                dt = offlineDataModel.dt, sunrise = offlineDataModel.sunrise,
                sunset = offlineDataModel.sunset,
                temp = offlineDataModel.temp,
                feels_like = offlineDataModel.feels_like,
                pressure = offlineDataModel.pressure,
                humidity = offlineDataModel.humidity,
                dew_point = offlineDataModel.dew_point,
                uvi = offlineDataModel.uvi,
                clouds = offlineDataModel.clouds,
                visibility = offlineDataModel.visibility,
                wind_speed = offlineDataModel.wind_speed,
                wind_deg = offlineDataModel.wind_deg,
                wind_gust = offlineDataModel.wind_gust,
                weather = arrayListOf(
                    Weather(
                        description = offlineDataModel.description,
                        icon = offlineDataModel.icon
                    )
                )
            )
        )
        weatherData.area = offlineDataModel.area
        return weatherData
    }
}
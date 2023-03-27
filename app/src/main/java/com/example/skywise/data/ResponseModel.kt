package com.example.skywise.data

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

data class WeatherData(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val timezone: String? = null,
    val timezone_offset: Long = 0,
    val current: Current = Current(),
    val minutely: ArrayList<Minutely>? = null,
    val hourly: ArrayList<Hourly>? = null,
    val daily: ArrayList<Daily>? = null,
    val alerts: ArrayList<Alert>? = null
) {
    fun getCurrentLocation(): String {
        return timezone ?: "N/A"
    }
}


data class Current(
    val dt: Long = 0,
    val sunrise: Long = 0,
    val sunset: Long = 0,
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val pressure: Int = 0,
    val humidity: Int = 0,
    val dew_point: Double = 0.0,
    val uvi: Double = 0.0,
    val clouds: Int = 0,
    val visibility: Int = 0,
    val wind_speed: Double = 0.0,
    val wind_deg: Int = 0,
    val wind_gust: Double = 0.0,
    val weather: ArrayList<Weather> = arrayListOf(Weather())
) {

    fun getUviText(): String {
        return uvi.toString()
    }

    fun getHumidityText(): String {
        return humidity.toString()
    }

    fun getWindSpeedText(): String {
        return wind_speed.toString()
    }

    fun getFeelsLikeText(): String {
        return feels_like.roundToInt().toString() + '°'
    }

    fun getTempText(): String {
        return temp.roundToInt().toString() + '°'
    }

    fun getSunriseText(): String {
        return getTimeText(sunrise)
    }

    fun getSunsetText(): String {
        return getTimeText(sunset)
    }

    fun getCurrentTimeText(): String {
        return getTimeText(dt)
    }

    private fun getTimeText(sunTime: Long): String {
        val dtf =
            DateTimeFormatter.ofPattern("h:mm a").withLocale(Locale(Locale.getDefault().language))
        return Instant.ofEpochSecond(sunTime).atZone(ZoneId.systemDefault()).format(dtf)
    }
}

data class Weather(
    val id: Int = 0,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = "10d"
) {

}

data class Minutely(
    val dt: Long = 0, val precipitation: Int = 0
) {}

data class Hourly(
    val dt: Long = 0,
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val pressure: Long = 0,
    val humidity: Long = 0,
    val dew_point: Double = 0.0,
    val uvi: Double = 0.0,
    val clouds: Long = 0,
    val visibility: Long = 0,
    val wind_speed: Double = 0.0,
    val wind_deg: Long = 0,
    val wind_gust: Double = 0.0,
    val weather: ArrayList<Weather>? = null,
    val pop: Double = 0.0,
    val rain: Rain? = null
) {

    fun getPopText(): String {
        return (pop * 100).roundToInt().toString() + '%'
    }

    fun getTempText(): String {
        return "$temp°"
    }

    fun getTimeText(): String {
        val dtf = DateTimeFormatter.ofPattern("ha").withLocale(Locale(Locale.getDefault().language))
        return Instant.ofEpochSecond(this.dt).atZone(ZoneId.systemDefault()).format(dtf)
    }

}

data class Daily(
    val dt: Long = 0,
    val sunrise: Long = 0,
    val sunset: Long = 0,
    val moonrise: Long = 0,
    val moonset: Long = 0,
    val moon_phase: Double = 0.0,
    val temp: Temp? = null,
    val feels_like: FeelsLike? = null,
    val pressure: Int = 0,
    val humidity: Int = 0,
    val dew_point: Double = 0.0,
    val wind_speed: Double = 0.0,
    val wind_deg: Int = 0,
    val wind_gust: Double = 0.0,
    val weather: ArrayList<Weather>? = null,
    val clouds: Int = 0,
    val pop: Double = 0.0,
    val rain: Double = 0.0,
    val uvi: Double = 0.0
) {

    fun getPopText(): String {
        return (pop * 100).roundToInt().toString() + '%'
    }

    fun getDayText(): String {
        val dtf = DateTimeFormatter.ofPattern("EEE dd/MM")
            .withLocale(Locale(Locale.getDefault().language))
        return Instant.ofEpochSecond(this.dt).atZone(ZoneId.systemDefault()).format(dtf)
    }

}

data class Temp(
    val day: Double = 0.0,
    val min: Double = 0.0,
    val max: Double = 0.0,
    val night: Double = 0.0,
    val eve: Double = 0.0,
    val morn: Double = 0.0
) {
    fun getMaxText(): String {
        return max.roundToInt().toString() + '°'
    }

    fun getMinText(): String {
        return min.roundToInt().toString() + '°'
    }
}

data class Rain(
    val _1h: Double = 0.0
) {}

data class FeelsLike(
    val day: Double = 0.0, val night: Double = 0.0, val eve: Double = 0.0, val morn: Double = 0.0
) {

}

data class Alert(
    val sender_name: String? = null,
    val event: String? = null,
    val start: Long = 0,
    val end: Long = 0,
    val description: String? = null,
    val tags: ArrayList<String>? = null
) {

}
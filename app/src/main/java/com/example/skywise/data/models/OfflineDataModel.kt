package com.example.skywise.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.skywise.domain.settings.SkywiseSettings
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@Entity(tableName = "offline_data")
data class OfflineDataModel(
    val lat: Double,
    val lon: Double,
    val timezone_offset: Long = 0,
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
    val description: String? = "No Desc",
    val icon: String? = "10d",
    val area: String = "location, name"
) {
    @PrimaryKey
    var date: String = LocalDate.now().toString()

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
            DateTimeFormatter.ofPattern("h:mm a").withLocale(Locale(SkywiseSettings.lang))
        return Instant.ofEpochSecond(sunTime).atZone(ZoneId.systemDefault()).format(dtf)
    }
}


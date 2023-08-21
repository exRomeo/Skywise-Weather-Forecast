package com.example.skywise.data.models

import androidx.room.Entity
import java.time.LocalDate

@Entity(tableName = "favorite_locations", primaryKeys = ["lat", "lon"])
data class FavoriteLocation(
    val lat: Double,
    val lon: Double,
    var area: String = "",
    var currentLocation: Boolean = false
) {

    var date = LocalDate.now().toString()
}
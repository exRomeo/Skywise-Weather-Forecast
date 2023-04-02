package com.example.skywise.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "favorite_locations")
data class FavoriteLocation(
    val lat: Double,
    val lon: Double,
    var area: String = "",
    var currentLocation: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var date = LocalDate.now().toString()
}
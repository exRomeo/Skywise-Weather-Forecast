package com.example.skywise.utils

import android.location.Address
import android.location.Geocoder

object GeocoderUtil {

    private lateinit var geocoder: Geocoder

    fun initialize(geocoder: Geocoder) {
        this.geocoder = geocoder
    }

    fun getLocationName(lat: Double, lon: Double): String {
        if (::geocoder.isInitialized) {
            val addresses = geocoder.getFromLocation(lat, lon, 1)

            val address =
                if (!addresses.isNullOrEmpty())
                    geocoder.getFromLocation(lat, lon, 1)?.get(0)
                else
                    null


            return "${address?.locality ?: ""} ${
                address?.adminArea?.replace(
                    " Governorate",
                    ""
                ) ?: ""
            }"/*, ${address?.countryCode ?: ""}*/
        } else return ""
    }

}
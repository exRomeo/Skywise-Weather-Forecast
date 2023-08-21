package com.example.skywise.presentation.ui.mapsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywise.R
import com.example.skywise.data.models.FavoriteLocation
import com.example.skywise.data.repository.Repository
import com.example.skywise.domain.settings.SkywiseSettings
import com.example.skywise.domain.utils.GeocoderUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint
import javax.inject.Inject

@HiltViewModel
class MapSheetViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val _snackBarText = MutableSharedFlow<Int>()
    val snackBarText = _snackBarText.asSharedFlow()
    fun addLocation(iGeoPoint: IGeoPoint) {
        viewModelScope.launch {
            val lat = iGeoPoint.latitude
            val lon = iGeoPoint.longitude
            val area = GeocoderUtil.getLocationName(lat, lon)
            repository.addLocation(
                FavoriteLocation(
                    lat = lat,
                    lon = lon,
                    area = area
                )
            )
            _snackBarText.emit(R.string.location_saved)
        }
    }

    fun updateLocation(iGeoPoint: IGeoPoint) {
        SkywiseSettings.setLatitude(iGeoPoint.latitude)
        SkywiseSettings.setLongitude(iGeoPoint.longitude)
        SkywiseSettings.requiresLocation = false
    }
}
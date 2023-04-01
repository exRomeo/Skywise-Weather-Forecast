package com.example.skywise.mapsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywise.R
import com.example.skywise.data.FavoriteLocation
import com.example.skywise.data.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint

class MapSheetViewModel(val repository: Repository) : ViewModel() {

    private val _snackBarText = MutableSharedFlow<Int>()
    val snackBarText = _snackBarText.asSharedFlow()
    fun addLocation(iGeoPoint: IGeoPoint) {
        viewModelScope.launch {
            repository.addLocation(
                FavoriteLocation(
                    lat = iGeoPoint.latitude,
                    lon = iGeoPoint.longitude
                )
            )
            _snackBarText.emit(R.string.location_saved)
        }
    }
}
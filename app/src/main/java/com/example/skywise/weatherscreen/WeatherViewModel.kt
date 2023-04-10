package com.example.skywise.weatherscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywise.R
import com.example.skywise.data.Repository
import com.example.skywise.data.WeatherDTO
import com.example.skywise.data.remotesource.API_KEY
import com.example.skywise.settingsscreen.SkywiseSettings
import com.example.skywise.settingsscreen.SkywiseSettings.MINUTELY
import com.example.skywise.utils.ConnectionUtils
import com.example.skywise.utils.DataUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: Repository) : ViewModel() {
    private var _weatherDTO = MutableStateFlow<WeatherDTO>(WeatherDTO.Loading)
    val weatherDTO = _weatherDTO.asStateFlow()

    private val _snackBarText = MutableSharedFlow<Int>()
    val snackBarText = _snackBarText.asSharedFlow()
    fun updateData() {
        viewModelScope.launch {

            if (weatherDTO.value is WeatherDTO.Failure) {
                _weatherDTO.value = WeatherDTO.Loading
            }

            if (ConnectionUtils.checkConnection()) {
                repository.getLocationData(
                    SkywiseSettings.lat,
                    SkywiseSettings.lon,
                    SkywiseSettings.lang,
                    SkywiseSettings.units,
                    MINUTELY,
                    API_KEY
                ).catch {

                    _weatherDTO.value = WeatherDTO.Failure(it)
                    _snackBarText.emit(R.string.data_not_retrieved)
                }.collectLatest {
                    _weatherDTO.value = WeatherDTO.SuccessOnline(it)

                }
            } else {
                val offlineData = repository.getOfflineData()

                _weatherDTO.value =
                    WeatherDTO.SuccessOffline(DataUtils.offlineDataToOnlineData(offlineData))
                _snackBarText.emit(R.string.showing_offline_data)
            }

        }
    }

    fun update() {
        updateData()
        viewModelScope.launch {
            _snackBarText.emit(R.string.updating_data)
        }
    }

}
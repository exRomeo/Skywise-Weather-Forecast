package com.example.skywise.presentation.ui.weatherscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skywise.R
import com.example.skywise.data.repository.Repository
import com.example.skywise.domain.settings.SkywiseSettings
import com.example.skywise.domain.settings.SkywiseSettings.MINUTELY
import com.example.skywise.domain.utils.ConnectionUtils
import com.example.skywise.domain.utils.DataUtils
import com.example.skywise.presentation.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var _UIState = MutableStateFlow<UIState>(UIState.Loading)
    val weatherDTO = _UIState.asStateFlow()

    private val _snackBarText = MutableSharedFlow<Int>()
    val snackBarText = _snackBarText.asSharedFlow()
    fun updateData() {
        viewModelScope.launch {

            if (weatherDTO.value is UIState.Failure) {
                _UIState.value = UIState.Loading
            }

            if (ConnectionUtils.checkConnection()) {
                repository.getLocationData(
                    SkywiseSettings.lat,
                    SkywiseSettings.lon,
                    SkywiseSettings.lang,
                    SkywiseSettings.units,
                    MINUTELY
                ).catch {

                    _UIState.value = UIState.Failure(it)
                    _snackBarText.emit(R.string.data_not_retrieved)
                }.collectLatest {
                    _UIState.value = UIState.SuccessOnline(it)

                }
            } else {
                val offlineData = repository.getOfflineData()

                _UIState.value =
                    UIState.SuccessOffline(DataUtils.offlineDataToOnlineData(offlineData))
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
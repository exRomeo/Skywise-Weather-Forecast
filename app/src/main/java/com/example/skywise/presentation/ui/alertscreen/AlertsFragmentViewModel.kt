package com.example.skywise.presentation.ui.alertscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.skywise.data.models.WeatherAlert
import com.example.skywise.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsFragmentViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _alertsList: MutableStateFlow<List<WeatherAlert>> = MutableStateFlow(listOf())
    val alertsList = _alertsList.asStateFlow()

    init {
        getAlertList()
    }

    private fun getAlertList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllAlerts().collect { _alertsList.value = it }
        }
    }

    private var _alertID = MutableSharedFlow<Long>()
    val alertID = _alertID.asSharedFlow()
    fun addWeatherAlert(alert: WeatherAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            _alertID.emit(repository.addWeatherAlert(alert))
        }
    }

    fun removeWeatherAlert(alert: WeatherAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeAlert(alert)
            WorkManager.getInstance().cancelAllWorkByTag("${alert.id}")
        }
    }


}

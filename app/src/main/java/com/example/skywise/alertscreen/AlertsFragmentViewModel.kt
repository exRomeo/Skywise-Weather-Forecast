package com.example.skywise.alertscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.skywise.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlertsFragmentViewModel(private val repository: Repository) : ViewModel() {
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

class AlertsFragmentViewModelFactory(private val repository: Repository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertsFragmentViewModel::class.java)) AlertsFragmentViewModel(
            repository
        ) as T else throw IllegalArgumentException("View Model Class Not Found!!!")
    }
}
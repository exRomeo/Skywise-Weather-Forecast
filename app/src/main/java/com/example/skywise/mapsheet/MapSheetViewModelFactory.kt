package com.example.skywise.mapsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.skywise.data.Repository

@Suppress("UNCHECKED_CAST")
class MapSheetViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return if (modelClass.isAssignableFrom(MapSheetViewModel::class.java)) MapSheetViewModel(
            repository
        ) as T
        else throw IllegalArgumentException("View Model Class Not Found!!!")
    }
}
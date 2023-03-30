package com.example.skywise.favoritelocations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skywise.data.Repository

class FavoriteLocationViewModelFactory(private val repository: Repository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteLocationsViewModel::class.java)) FavoriteLocationsViewModel(
            repository
        ) as T else throw IllegalArgumentException("View Model Class Not Found!!!")
    }
}
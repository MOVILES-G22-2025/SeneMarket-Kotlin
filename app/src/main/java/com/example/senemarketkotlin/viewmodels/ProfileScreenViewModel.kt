package com.example.senemarketkotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.senemarketkotlin.models.DataLayerFacade

class ProfileScreenViewModel (
    private val dataLayerFacade: DataLayerFacade
) : ViewModel() {

    class Factory(
        private val dataLayerFacade: DataLayerFacade,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileScreenViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileScreenViewModel(dataLayerFacade) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
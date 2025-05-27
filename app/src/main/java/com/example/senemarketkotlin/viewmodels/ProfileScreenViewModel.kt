package com.example.senemarketkotlin.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.senemarketkotlin.models.DataLayerFacade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow

class ProfileScreenViewModel (
    private val dataLayerFacade: DataLayerFacade
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    init {
        viewModelScope.launch {
            try {
                val currentUser = dataLayerFacade.getCurrentUser()
                if (currentUser != null) {
                    _userName.value = currentUser.name.toString()
                }
            } catch (e: Exception) {
                Log.e("ProfileScreenViewModel", "Error fetching user", e)
            }
        }
    }

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
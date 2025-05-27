package com.example.senemarketkotlin.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.senemarketkotlin.models.DataLayerFacade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val dataLayerFacade: DataLayerFacade
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _semester = MutableStateFlow("")
    val semester: StateFlow<String> = _semester

    private val _career = MutableStateFlow("")
    val career: StateFlow<String> = _career

    init {
        // Carga inicial del usuario actual
        viewModelScope.launch {
            try {
                val user = dataLayerFacade.getCurrentUser()
                user?.let {
                    _name.value = it.name ?: ""
                    _semester.value = it.semester ?: ""
                    _career.value = it.career ?: ""
                }
            } catch (e: Exception) {
                Log.e("EditProfileVM", "Error loading user: ${e.message}")
            }
        }
    }

    fun saveProfile(newName: String, newSemester: String, newCareer: String) {
        viewModelScope.launch {
            try {
                val currentProfile = dataLayerFacade.getCurrentUser()

                val updatedName = if (newName.isNotBlank()) newName else currentProfile?.name
                val updatedSemester = if (newSemester.isNotBlank()) newSemester else currentProfile?.semester
                val updatedCareer = if (newCareer.isNotBlank()) newCareer else currentProfile?.career

                val updatedProfile = currentProfile?.copy(
                    name = updatedName,
                    semester = updatedSemester,
                    career = updatedCareer
                )

                if (updatedName != null && updatedSemester != null && updatedCareer != null) {
                    dataLayerFacade.updateUser(updatedName, updatedSemester, updatedCareer)
                    _name.value = updatedName
                    _semester.value = updatedSemester
                    _career.value = updatedCareer
                }

            } catch (e: Exception) {
                Log.e("EditProfileViewModel", "Error updating profile", e)
            }
        }
    }


    // Factory
    class Factory(private val dataLayerFacade: DataLayerFacade) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditProfileViewModel(dataLayerFacade) as T
        }
    }
}

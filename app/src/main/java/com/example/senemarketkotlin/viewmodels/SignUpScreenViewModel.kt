package com.example.senemarketkotlin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.senemarketkotlin.models.DataLayerFacade
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SignUpScreenViewModel (
    private val navController: NavController,
    private val dataLayerFacade: DataLayerFacade
): ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> = _confirmPassword

    private val _career = MutableLiveData<String>()
    val career: LiveData<String> = _career

    private val _semester = MutableLiveData<String>()
    val semester: LiveData<String> = _semester

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> = _fullName

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun goToLogin() {
        this.navController.navigate("login")

    }

    fun register() {
        val fullNameValue = _fullName.value.orEmpty()
        val emailValue = _email.value.orEmpty()
        val passwordValue = _password.value.orEmpty()
        val confirmPasswordValue = _confirmPassword.value.orEmpty()
        val careerValue = _career.value.orEmpty()
        val semesterValue = _semester.value.orEmpty()

        // Validaciones
        when {
            !emailValue.endsWith("@uniandes.edu.co") -> {
                _error.value = "The email must be a Uniandes email (@uniandes.edu.co)"
                return
            }
            semesterValue.toIntOrNull() == null || semesterValue.toInt() >= 15 || semesterValue.toInt() <= 0 -> {
                _error.value = "The semester must be a number between 1 and 15"
                return
            }
            passwordValue != confirmPasswordValue -> {
                _error.value = "The passwords do not match"
                return
            }
            else -> {
                _error.value = "" // Limpia errores
            }
        }

        viewModelScope.launch {
            try {
                dataLayerFacade.signUp(emailValue, passwordValue, fullNameValue, careerValue, semesterValue)
                navController.navigate ("home")
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("email", ignoreCase = true) == true ->
                        "This email has already register, please sign in or try with a different email."
                    else -> "${e.message}"
                }
                _error.value = errorMessage
            }
        }

    }
    fun goToInitial(){
        this.navController.navigate("initial")
    }

    fun onSignUpPasswordChange(password: String) {
        _password.value = password
    }

    fun onSignUpConfirmPasswordChange(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun onSignUpEmailChange(email: String) {
        _email.value = email
    }

    fun onSignUpCareerChange(career: String) {
        _career.value = career
    }
    fun onSignUpSemesterChange(semester: String) {
        _semester.value = semester
    }

    fun onSignUpFullNameChange(fullName: String) {
        _fullName.value = fullName
    }
}

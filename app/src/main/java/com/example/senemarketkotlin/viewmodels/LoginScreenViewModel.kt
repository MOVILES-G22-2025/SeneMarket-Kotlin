package com.example.senemarketkotlin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.senemarketkotlin.models.DataLayerFacade
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.launch

class LoginScreenViewModel (
    private val navController: NavController,
    private val dataLayerFacade: DataLayerFacade
    ): ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun goToSignUp(){
        navController.navigate("signup")
    }

    fun goToLogin(){
        this.navController.navigate("login")

    }

    fun goToHome(){
        this.navController.navigate("home")
    }

    fun goToInitial(){
        this.navController.navigate("initial")
    }


    fun login(){
        viewModelScope.launch {
            try {

                val emailValue = _email.value.orEmpty()
                val passwordValue = _password.value.orEmpty()


                dataLayerFacade.login(emailValue, passwordValue)
                navController.navigate ("home")


            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _error.value= "Your sign in credentials are incorrect, please check and try again"
            } catch (e: Exception) {
                _error.value = "Authentication Error: ${e.message}"



            }
       }

    }

    fun onLoginPasswordChange(password: String) {
        _password.value = password

    }

    fun onLoginEmailChange(email: String) {
        _email.value = email
    }
}
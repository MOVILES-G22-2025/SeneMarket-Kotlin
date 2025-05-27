package com.example.senemarketkotlin.viewmodels

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.senemarketkotlin.repositories.ConnectivityRepository

class InitialScreenViewModel (
    private val navController: NavController,
    private val context: Context

): ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun goToLogin(){
        val connected = ConnectivityRepository.isOnline(context)
        if(!connected){
            _error.value= "You are not currently connected to the Internet, please check your connection and try again!"
        }
        else {
            this.navController.navigate("login")
        }

    }

    fun goToSignUp(){
        val connected = ConnectivityRepository.isOnline(context)
        if(!connected){
            _error.value= "You are not currently connected to the Internet, please check your connection and try again!"
        }
        else {
            this.navController.navigate("signup")
        }


    }
}
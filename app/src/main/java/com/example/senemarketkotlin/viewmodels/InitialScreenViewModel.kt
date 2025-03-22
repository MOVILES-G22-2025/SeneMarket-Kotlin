package com.example.senemarketkotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

class InitialScreenViewModel (
    private val navController: NavController
): ViewModel() {

    fun goToLogin(){
        this.navController.navigate("login")


    }

    fun goToSignUp(){
        navController.navigate("signup")

    }
}
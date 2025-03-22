package com.example.senemarketkotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class SignUpScreenViewModel (
    private val navController: NavController,
    private val auth: FirebaseAuth
): ViewModel() {

    fun goToLogin() {
        this.navController.navigate("login")

    }

    fun register() {
        this.navController.navigate ("home")
    }
    fun goToInitial(){
        this.navController.navigate("initial")
    }

}

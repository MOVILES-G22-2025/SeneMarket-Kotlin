package com.example.senemarketkotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

class LoginScreenViewModel (
    private val navController: NavController,
    private val auth: FirebaseAuth
    ): ViewModel() {

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


    fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            if (it.user != null)
            {
                navController.navigate("home")
            }
        }

    }
}
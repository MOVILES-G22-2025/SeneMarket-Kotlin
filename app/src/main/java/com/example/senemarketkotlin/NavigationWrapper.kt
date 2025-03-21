package com.example.senemarketkotlin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.senemarketkotlin.ui.screens.initial.InitialScreen
import com.example.senemarketkotlin.ui.screens.login.LoginScreen
import com.example.senemarketkotlin.ui.screens.signup.SignUpScreen
import com.google.firebase.auth.FirebaseAuth

@Composable

fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth){

    NavHost(navController = navHostController , startDestination = "initial"){
        composable("initial"){
            InitialScreen(
                navigateToLogin = {navHostController.navigate("login")},
                navigateToSignUp = {navHostController.navigate("signup")}
            )
        }
        composable("login"){
            LoginScreen(auth)
        }
        composable("signup"){
            SignUpScreen(auth)
        }

    }
}
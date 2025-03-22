package com.example.senemarketkotlin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.senemarketkotlin.ui.screens.home.HomeScreen
import com.example.senemarketkotlin.ui.screens.initial.InitialScreen
import com.example.senemarketkotlin.ui.screens.login.LoginScreen
import com.example.senemarketkotlin.ui.screens.signup.SignUpScreen
import com.example.senemarketkotlin.viewmodels.InitialScreenViewModel
import com.example.senemarketkotlin.viewmodels.LoginScreenViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable

fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth){

    NavHost(navController = navHostController , startDestination = "initial"){
        composable("initial"){
            InitialScreen(
                viewModel = InitialScreenViewModel(navHostController)
            )
        }
        composable("login"){
            LoginScreen(viewModel = LoginScreenViewModel(navHostController, auth))
        }
        composable("signup"){
            SignUpScreen(auth)
        }
        composable("home") {
            HomeScreen()
        }
    }
}
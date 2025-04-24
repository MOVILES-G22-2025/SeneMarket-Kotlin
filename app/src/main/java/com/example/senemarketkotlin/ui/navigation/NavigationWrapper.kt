package com.example.senemarketkotlin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.ui.screens.home.HomeScreen
import com.example.senemarketkotlin.ui.screens.home.ProductDetailScreen
import com.example.senemarketkotlin.ui.screens.initial.InitialScreen
import com.example.senemarketkotlin.ui.screens.login.LoginScreen
import com.example.senemarketkotlin.ui.screens.main.MainScreen
import com.example.senemarketkotlin.ui.screens.signup.SignUpScreen
import com.example.senemarketkotlin.ui.screens.splash.SplashScreen
import com.example.senemarketkotlin.viewmodels.InitialScreenViewModel
import com.example.senemarketkotlin.viewmodels.LoginScreenViewModel
import com.example.senemarketkotlin.viewmodels.SignUpScreenViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable

fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth, dataLayerFacade: DataLayerFacade){

    NavHost(navController = navHostController , startDestination = "splash"){
        composable("splash") { SplashScreen(navHostController) }
        composable("initial"){
            InitialScreen(
                viewModel = InitialScreenViewModel(navHostController)
            )
        }
        composable("login"){
            LoginScreen(viewModel = LoginScreenViewModel(navHostController, dataLayerFacade))
        }
        composable("signup"){
            SignUpScreen(viewModel = SignUpScreenViewModel(navHostController, dataLayerFacade))
        }
        composable("home") {
            MainScreen(navHostController, dataLayerFacade, index = 0)
        }

        composable("sell") {
            MainScreen(navHostController, dataLayerFacade, index = 2)
        }

        composable("favorites") {
            MainScreen(navHostController, dataLayerFacade, index = 3)
        }

        composable("profile") {
            MainScreen(navHostController, dataLayerFacade, index = 4)
        }

        composable(
            "productDetail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            ProductDetailScreen(productId, dataLayerFacade, navHostController)

        }
    }
}
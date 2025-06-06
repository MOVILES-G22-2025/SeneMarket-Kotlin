package com.example.senemarketkotlin

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.repositories.ProductRepository
import com.example.senemarketkotlin.repositories.StorageRepository
import com.example.senemarketkotlin.repositories.UserRepository
import com.example.senemarketkotlin.ui.navigation.NavigationWrapper
import com.example.senemarketkotlin.ui.theme.SeneMarketKotlinTheme
import com.example.senemarketkotlin.utils.PriceDropChecker
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userRepository: UserRepository

    private lateinit var storageRepository: StorageRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        db = Firebase.firestore
        userRepository = UserRepository( db = db, auth = auth)
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE)

        storageRepository = StorageRepository()
        productRepository = ProductRepository( db = db, auth = auth, sharedPreferences = sharedPreferences)

        val dataLayerFacade = DataLayerFacade(
            userRepository = userRepository,
            storageRepository = storageRepository,
          productRepository = productRepository)

     
       
        setContent {
            navHostController = rememberNavController()

            SeneMarketKotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationWrapper(navHostController,auth,dataLayerFacade = dataLayerFacade)
                }
            }
        }

        PriceDropChecker.appContext = applicationContext
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //navegar a la home
        }
    }
}




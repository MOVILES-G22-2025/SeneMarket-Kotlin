@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.senemarketkotlin.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.senemarketkotlin.ui.theme.White
import com.google.firebase.auth.FirebaseAuth
import com.example.senemarketkotlin.R


@Composable

fun LoginScreen(auth: FirebaseAuth) {


    Column(modifier = Modifier.fillMaxSize(). background(White)) {

        Icon(painter = painterResource(R.drawable.back), contentDescription = "")
        Text("Email", fontWeight = FontWeight.Bold, fontSize = 40.sp)



        Icon(painter = painterResource(R.drawable.back), contentDescription = "")
        Text("Email", fontWeight = FontWeight.Bold, fontSize = 40.sp)






    }
}
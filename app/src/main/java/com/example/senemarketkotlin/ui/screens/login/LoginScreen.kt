@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.senemarketkotlin.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.senemarketkotlin.viewmodels.LoginScreenViewModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.senemarketkotlin.ui.theme.Yellow30


@Composable

fun LoginScreen(viewModel: LoginScreenViewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Botón de regreso (Flecha)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back), // Carga la imagen desde drawable
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { viewModel.goToInitial()}
                )
            }
            Spacer(modifier = Modifier.weight(0.7f))
            // Título
            Text(
                text = "Welcome back",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campos de texto
            OutlinedTextField(
                value = "",
                onValueChange = { /* Actualizar valor */ },
                label = { Text("Uniandes email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "",
                onValueChange = { /* Actualizar valor */ },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Enlace "Forgot password?"
            Text(
                text = "Forgot password?",
                color = Color(0xFFF4C400),
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable { /* Acción para recuperar contraseña */ }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {viewModel.goToHome()}, modifier = Modifier
                    //.fillMaxWidth()
                    .padding(5.dp), colors = ButtonDefaults.buttonColors(containerColor = Yellow30)
            ) {
                Text(text = "Sign in", color = White)
            }

            Row {
                Text(text = "New to SeneMarket?", color = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Create account",
                    color = Color(0xFFF4C400), // Color amarillo similar
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.goToSignUp() }
                )
            }
            Spacer(modifier = Modifier.weight(0.7f))


    }
}
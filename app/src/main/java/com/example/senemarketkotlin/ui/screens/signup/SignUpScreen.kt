package com.example.senemarketkotlin.ui.screens.signup

import androidx.compose.runtime.Composable
import com.example.senemarketkotlin.viewmodels.LoginScreenViewModel
import com.example.senemarketkotlin.viewmodels.SignUpScreenViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.senemarketkotlin.R
import com.example.senemarketkotlin.ui.theme.Yellow30




@Composable
fun SignUpScreen(viewModel: SignUpScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back arrow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { viewModel.goToInitial() }
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        // Title
        Text(
            text = "Create account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Form fields
        val modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

        var fullName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var career by remember { mutableStateOf("") }
        var semester by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full name") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Uniandes email") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp)
        )

        OutlinedTextField(
            value = career,
            onValueChange = { career = it },
            label = { Text("Career") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp)
        )

        OutlinedTextField(
            value = semester,
            onValueChange = { semester = it },
            label = { Text("Semester") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm password") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Register button
        Button(
            onClick = { viewModel.register() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4C400)),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(5.dp)
                .height(48.dp)
        ) {
            Text(text = "Register", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Already have an account? Sign In
        Row {
            Text(text = "Already have an account?", color = Color.Black)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Sign In",
                color = Color(0xFFF4C400),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.goToLogin() }
            )
        }
        Spacer(modifier = Modifier.weight(0.3f))
    }
}


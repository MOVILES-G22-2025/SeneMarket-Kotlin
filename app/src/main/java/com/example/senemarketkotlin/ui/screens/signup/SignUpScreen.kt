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
import androidx.compose.runtime.livedata.observeAsState
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

        val email: String by viewModel.email.observeAsState(initial = "")
        val password: String by viewModel.password.observeAsState(initial = "")
        val confirmPassword: String by viewModel.confirmPassword.observeAsState(initial = "")
        val fullName: String by viewModel.fullName.observeAsState(initial = "")
        val career: String by viewModel.career.observeAsState(initial = "")
        val semester: String by viewModel.semester.observeAsState(initial = "")
        val error: String by viewModel.error.observeAsState(initial = "")


        OutlinedTextField(
            value = fullName,
            onValueChange = {viewModel.onSignUpFullNameChange(it)},
            label = { Text("Full name") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )
        )

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onSignUpEmailChange(it)},
            label = { Text("Uniandes email") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )
        )

        OutlinedTextField(
            value = career,
            onValueChange = { viewModel.onSignUpCareerChange(it) },
            label = { Text("Career") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )
        )

        OutlinedTextField(
            value = semester,
            onValueChange = { viewModel.onSignUpSemesterChange(it)},
            label = { Text("Semester") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onSignUpPasswordChange(it)},
            label = { Text("Password") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { viewModel.onSignUpConfirmPasswordChange(it) },
            label = { Text("Confirm password") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (error.isNotEmpty())
            Text("Error: $error")

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


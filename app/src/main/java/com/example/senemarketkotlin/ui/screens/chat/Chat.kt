package com.example.senemarketkotlin.ui.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.senemarketkotlin.R

import com.example.senemarketkotlin.ui.theme.White
import com.example.senemarketkotlin.ui.theme.Yellow30
import com.example.senemarketkotlin.viewmodels.InitialScreenViewModel

@Composable
fun InitialScreen(viewModel: InitialScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.weight(0.7f))
        Image(painter = painterResource(id = R.drawable.senemarket), contentDescription = null)
        Text("SeneMarket.", fontSize = 38.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Find everything you need for your career in one place",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 25.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {viewModel.goToLogin()}, modifier = Modifier
                //.fillMaxWidth()
                .padding(5.dp), colors = ButtonDefaults.buttonColors(containerColor = Yellow30)
        ) {
            Text(text = "Sign in", color = White)
        }

        Row {
            Text(text = "Already have an account?", color = Color.Black)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Create account",
                color = Color(0xFFF4C400), // Color amarillo similar
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.goToSignUp() }
            )
        }
    }
}


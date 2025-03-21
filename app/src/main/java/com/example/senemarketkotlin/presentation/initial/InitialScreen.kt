package com.example.senemarketkotlin.presentation.initial

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.senemarketkotlin.R
import com.example.senemarketkotlin.ui.theme.Black

import com.example.senemarketkotlin.ui.theme.White
import com.example.senemarketkotlin.ui.theme.Yellow30
import java.nio.file.WatchEvent

@Preview
@Composable
fun InitialScreen( navigateToLogin:() -> Unit = {}, navigateToSignUp:() -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.weight(0.7f))
        Image(painter = painterResource(id = R.drawable.senemarket), contentDescription = null)
        Text("SeneMarket", fontSize = 38.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "Find everything you need for your career in one place",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 25.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {navigateToSignUp()}, modifier = Modifier
                //.fillMaxWidth()
                .padding(5.dp), colors = ButtonDefaults.buttonColors(containerColor = Yellow30)
        ) {
            Text(text = "Sign in", color = White)
        }

        Text("New to SeneMarket? Create account", modifier = Modifier.clickable { navigateToLogin() })
        Spacer(modifier = Modifier.weight(0.7f))
    }
}


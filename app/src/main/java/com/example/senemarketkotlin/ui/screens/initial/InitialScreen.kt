package com.example.senemarketkotlin.ui.screens.initial

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

    val error: String by viewModel.error.observeAsState(initial = "")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.weight(0.7f))
        Image(painter = painterResource(id = R.drawable.senemarket), contentDescription = null)
        Spacer(modifier = Modifier.weight(0.1f))
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
            onClick = {viewModel.goToLogin()}, modifier = Modifier
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

        //if (error.isNotEmpty())
        //Text("$error", color = Color.Red)

        if (error.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0E0)), // fondo rosado claro
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {

                    Image(painter = painterResource(id = R.drawable.ic_no_wifi), contentDescription = null)

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = error,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


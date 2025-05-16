package com.example.senemarketkotlin.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.senemarketkotlin.R
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.viewmodels.ProfileScreenViewModel
import androidx.compose.material.icons.filled.Settings as Settings

@Composable
fun ProfileScreen (dataLayerFacade: DataLayerFacade, navController: NavController) {
    val viewModel: ProfileScreenViewModel = viewModel(factory = ProfileScreenViewModel.Factory(dataLayerFacade))

    //val user by viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp)
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Profile",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif
            )
        }

        // Profile Picture and Greeting
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Hello, User",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        // Botones del perfil
        val iconColor = Color(0xFFFFC928)

        val options = listOf(
            "Edit profile" to Icons.Default.Edit,
            "My products" to Icons.Default.ShoppingCart,
            "My drafts" to Icons.Default.Settings,
            "Favorites" to Icons.Default.Settings,
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f, fill = true) // Ocupa espacio y empuja el botón de logout hacia abajo
        ) {
            options.forEach { (text, icon) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            Color(0xFFF2F1F7),
                            shape = RoundedCornerShape(topStart = 0.dp, topEnd = 30.dp, bottomEnd = 30.dp, bottomStart = 0.dp)
                        )
                        .clickable { /* TODO: handle navigation if needed */ }
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icon, contentDescription = text, tint = iconColor)
                    Spacer(modifier = Modifier.width(18.dp))
                    Text(text, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowForward, contentDescription = "Go", tint = Color.Black)
                }
            }
        }

        // Botón Logout fijo abajo a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    //viewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", color = Color.White)
            }
        }
    }
}
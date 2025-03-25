package com.example.senemarketkotlin.ui.screens.sell

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.senemarketkotlin.R
import com.example.senemarketkotlin.ui.theme.White
import com.example.senemarketkotlin.ui.theme.Yellow30

@Composable
fun SellScreen () {
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
                    .clickable { }
            )
        }

        Text("Add product", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))



        val modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

        var nameProduct by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }


        OutlinedTextField(
            value = nameProduct,
            onValueChange = {   },
            label = { Text("Name") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )

        )

        OutlinedTextField(
            value = description,
            onValueChange = {   },
            label = { Text("Description") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )
        )

        OutlinedTextField(
            value = category,
            onValueChange = {   },
            label = { Text("Category") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )
        )

        OutlinedTextField(
            value = price,
            onValueChange = {   },
            label = { Text("Price") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107), // Amarillo cuando está enfocado
                unfocusedBorderColor = Color.Black // Gris cuando no está enfocado
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {}, modifier = Modifier
                //.fillMaxWidth()
                .padding(5.dp), colors = ButtonDefaults.buttonColors(containerColor = Yellow30)
        ) {
            Text(text = "Add", color = White)
        }


        Spacer(modifier = Modifier.weight(2f))


    }
}
        // Title

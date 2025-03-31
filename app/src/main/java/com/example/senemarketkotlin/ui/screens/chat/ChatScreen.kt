package com.example.senemarketkotlin.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatScreen() {
    var message by remember { mutableStateOf(TextFieldValue()) }
    var messages by remember { mutableStateOf(listOf("Hola!" to false, "¿Cómo estás?" to true)) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Lista de mensajes
        Column(modifier = Modifier.weight(1f)) {
            messages.forEach { (msg, isSent) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start
                ) {
                    Text(
                        text = msg,
                        fontSize = 16.sp,
                        color = if (isSent) Color.White else Color.Black,
                        modifier = Modifier
                            .background(
                                color = if (isSent) Color(0xFF64B5F6) else Color(0xFFBDBDBD),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                            .widthIn(max = 250.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Campo de entrada y botón de enviar
        Row(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f).padding(8.dp)
            )
            Button(
                onClick = {
                    if (message.text.isNotBlank()) {
                        messages = messages + (message.text to true)
                        message = TextFieldValue()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Enviar", color = Color.White)
            }
        }
    }
}
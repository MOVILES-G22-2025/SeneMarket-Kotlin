package com.example.senemarketkotlin.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.senemarketkotlin.R

@Composable
fun ChatScreen() {
    var message by remember { mutableStateOf(TextFieldValue()) }
    var messages by remember { mutableStateOf(listOf("Nos vemos en el ML?" to false, "a las 11?" to true)) }
    var userName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Nombre del usuario en la parte superior

        Text(
            text = "Nicolas",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de mensajes
        Column(modifier = Modifier.weight(1f)) {
            messages.forEach { (msg, isSent) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isSent) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar),
                            contentDescription = "Avatar recibido",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = msg,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .background(
                                color = if (isSent) Color(0xFFF5C508) else Color(0xFFBDBDBD),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp)
                            .widthIn(max = 250.dp)
                    )

                    if (isSent) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar),
                            contentDescription = "Avatar enviado",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Campo de entrada y botón de enviar con icono
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 12.dp, vertical = 78.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 18.dp)
            ) {
                BasicTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (message.text.isNotBlank()) {
                            messages = messages + (message.text to true)
                            message = TextFieldValue()
                        }
                    })
                )
            }
            IconButton(
                onClick = {
                    if (message.text.isNotBlank()) {
                        messages = messages + (message.text to true)
                        message = TextFieldValue()
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "Enviar",
                    tint = Color(0xFFF5C508)
                    ,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

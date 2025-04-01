package com.example.app.models

data class Chat(
    val user1: String = "",         // Email del primer usuario
    val user2: String = "",         // Email del segundo usuario
    val messages: List<Message> = emptyList() // Lista de mensajes en el chat
)

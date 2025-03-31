package com.example.senemarketkotlin.models

import android.net.Uri

data class ProductModel(
    val name: String,
    val description: String,
    val category: String,
    val price: Int,
    val imageUrls: List<String?> = emptyList(),
    val imagePortada: String? = null,
    val favoritedBy: List<String> = emptyList(),
    val userId: String? = null,
    val sellerName: String? = null
)

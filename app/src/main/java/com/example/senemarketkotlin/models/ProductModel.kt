package com.example.senemarketkotlin.models

data class ProductModel(
    val category: String? = null,
    val description: String? = null,
    val favoritedBy: List<String>,
    val imagePortada: String? = null,
    val imageUrls: List<String>,
    val name: String? = null,
    val price: Double? = null,
    val sellerName: String? = null,
    val timestamp: String? = null,
    val userId: String? = null,
    )
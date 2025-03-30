package com.example.senemarketkotlin.models

import com.google.firebase.Timestamp

data class ProductModel(
    var category: String? = null,
    var description: String? = null,
    var favoritedBy: List<String>? = null,
    var imagePortada: String? = null,
    var imageUrls: List<String>? = null,
    var name: String? = null,
    var price: Any? = null,
    var sellerName: String? = null,
    var timestamp: Timestamp? = null,
    var userId: String? = null
) {
    constructor() : this(
        category = null,
        description = null,
        favoritedBy = null,
        imagePortada = null,
        imageUrls = null,
        name = null,
        price = null,
        sellerName = null,
        timestamp = null,
        userId = null
    )
}

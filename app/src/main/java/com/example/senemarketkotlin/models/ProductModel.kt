package com.example.senemarketkotlin.models
import android.net.Uri
import com.example.senemarketkotlin.utils.FirebaseTimestampSerializer
import kotlinx.serialization.*

import com.google.firebase.Timestamp

@Serializable
data class ProductModel(
    var id: String? = null,
    var category: String? = null,
    var description: String? = null,
    var favoritedBy: List<String>? = null,
    var imagePortada: String? = null,
    var imageUrls: List<String>? = null,
    var name: String? = null,
    var price: Int? = null,
    var sellerName: String? = null,
    @Serializable(with = FirebaseTimestampSerializer::class)
    var timestamp: Timestamp? = null,
    var userId: String? = null
) {
    constructor() : this(
        id = null,
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


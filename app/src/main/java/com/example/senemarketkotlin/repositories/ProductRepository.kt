package com.example.senemarketkotlin.repositories

import android.util.Log
import com.example.senemarketkotlin.models.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductRepository(private val db: FirebaseFirestore, private val auth: FirebaseAuth) {

    suspend fun getAllProducts(): List<ProductModel> {
        return try {
            val snapshot = db.collection("products").get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    val product = doc.toObject(ProductModel::class.java)

                    product?.price = when (val rawPrice = doc.get("price")) {
                        is Number -> rawPrice.toInt()  // Si es número, convertir a Double
                        is String -> rawPrice.toIntOrNull() ?: 0 // Si es String, intentar convertir
                        else -> 0  // Si es null u otro tipo, asignar 0.0
                    }

                    product
                } catch (e: Exception) {
                    Log.e("Dani", "Error deserializando producto: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("Dani", "Error obteniendo productos: ${e.message}")
            emptyList()
        }
    }
}
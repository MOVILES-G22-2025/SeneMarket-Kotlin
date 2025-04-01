package com.example.senemarketkotlin.repositories


import com.example.senemarketkotlin.models.ProductModel
import com.example.senemarketkotlin.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resumeWithException
import android.util.Log
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProductRepository(private val db: FirebaseFirestore, private val auth: FirebaseAuth) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun addProduct(productModel: ProductModel) {

        val userId = auth.currentUser?.uid ?: throw Exception("Failed to get user ID")

        println("USERID $userId")
        val userDoc = db.collection("users")
            .document(userId).get().await()
        val sellerName = userDoc.getString("name") ?: userDoc.getString("fullName")
        ?: throw Exception("Seller name not found")
        // Crear un nuevo modelo de producto con los campos adicionales
        val updatedProduct = productModel.copy(
            userId = userId,
            sellerName = sellerName,
            favoritedBy = emptyList() // Lista vacía de favoritos
        )
        val productRef = db.collection("products")
            .add(updatedProduct).await()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun login(email: String, password: String) {

        val authResult = suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    continuation.resume(result, null)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        val userId = authResult.user?.uid ?: throw Exception("Failed to get user ID")


    }

    suspend fun getAllProducts(): List<ProductModel> {
        return try {
            val snapshot = db.collection("products")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    val product = doc.toObject(ProductModel::class.java)
                    product?.id = doc.id

                    product?.price = when (val rawPrice = doc.get("price")) {
                        is Number -> rawPrice.toInt()
                        is String -> rawPrice.toIntOrNull() ?: 0
                        else -> 0
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

    private fun formatTimestamp(date: Date): String {
        val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
        return sdf.format(date)
    }

    suspend fun getProductById(productId: String): ProductModel? {
        return try {
            val snapshot = db.collection("products").document(productId).get().await()

            if (!snapshot.exists()) {
                Log.e("Dani", "Producto con ID $productId no encontrado en Firestore.")
                return null
            }

            val product = snapshot.toObject(ProductModel::class.java)?.apply {
                id = snapshot.id  // Asigna manualmente el ID
                price = when (val rawPrice = snapshot.get("price")) {
                    is Number -> rawPrice.toInt()
                    is String -> rawPrice.toIntOrNull() ?: 0
                    else -> 0
                }
            }

            product
        } catch (e: Exception) {
            Log.e("Dani", "Error obteniendo producto por ID: ${e.message}")
            null
        }
    }

    suspend fun searchProducts(query: String): List<ProductModel> {
        return try {
            if (query.isBlank()) {
                return getAllProducts()
            }

            val snapshot = db.collection("products")
                .orderBy("name")
                .whereGreaterThanOrEqualTo("name", query)
                .whereLessThanOrEqualTo("name", query + "\uf8ff")
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductModel::class.java)?.apply {
                    price = when (val rawPrice = doc.get("price")) {
                        is Number -> rawPrice.toInt()
                        is String -> rawPrice.toIntOrNull() ?: 0
                        else -> 0
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Dani", "Error en búsqueda de productos: ${e.message}")
            emptyList()
        }
    }
}


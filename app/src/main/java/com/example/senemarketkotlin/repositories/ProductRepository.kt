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

class ProductRepository(private val db: FirebaseFirestore, private val auth: FirebaseAuth){

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun addProduct(productModel: ProductModel) {

        val userId = auth.currentUser?.uid ?: throw Exception("Failed to get user ID")

        println("USERID $userId")
        val userDoc = db.collection("users")
            .document(userId).get().await()
        val sellerName = userDoc.getString("name") ?: userDoc.getString("fullName") ?: throw Exception("Seller name not found")
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

}


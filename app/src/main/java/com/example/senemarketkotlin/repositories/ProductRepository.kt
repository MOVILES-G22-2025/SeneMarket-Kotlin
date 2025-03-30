package com.example.senemarketkotlin.repositories

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
            val querySnapshot = withContext(Dispatchers.IO) {
                db.collection("products").get().await()
            }
            querySnapshot.documents.mapNotNull { it.toObject(ProductModel::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
package com.example.senemarketkotlin.repositories

import com.example.senemarketkotlin.models.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await

class ProductRepository(private val db: FirebaseFirestore, private val auth: FirebaseAuth) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getAllProducts(): List<ProductModel> {
        return try {
            db.collection("products")
                .get()
                .await()
                .documents
                .mapNotNull { snapshot ->
                    snapshot.toObject(ProductModel::class.java)
                }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
package com.example.senemarketkotlin.repositories

import android.util.Log
import com.example.senemarketkotlin.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resumeWithException

class UserRepository(private val db: FirebaseFirestore, private val auth: FirebaseAuth){

    val defaultCategories = listOf(
        "Academic materials", "Technology and electronics", "Transportation",
        "Clothing and accessories", "Housing", "Entertainment", "Sports and fitness"
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun signUp(email: String, password: String,  fullName: String, career: String, semester: String) {

        val authResult = suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    continuation.resume(result, null)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        val userId = authResult.user?.uid ?: throw Exception("Failed to get user ID")

        val user = UserModel(
            name = fullName,
            email = email,
            career = career,
            semester = semester,
        )


        suspendCancellableCoroutine { continuation ->

            println("USERID $userId")
            db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {

                    continuation.resume("Ok", null)

                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }


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

    suspend fun findUserById(userId: String): UserModel? {
        return try {
            val snapshot = db.collection("users").document(userId).get().await()

            if (!snapshot.exists()) {
                return null
            }

            snapshot.toObject(UserModel::class.java)?.apply {
                id = snapshot.id
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCurrentUser(): UserModel? {
        val userId = auth.currentUser?.uid ?: return null
        val doc = db.collection("users").document(userId).get().await()
        return doc.toObject(UserModel::class.java)
    }

    suspend fun toggleFavorite(productId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        val userRef = db.collection("users").document(userId)

        return try {
            db.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val currentFavorites = snapshot.get("favorites") as? List<String> ?: emptyList()
                val newFavorites = if (currentFavorites.contains(productId)) {
                    currentFavorites - productId
                } else {
                    currentFavorites + productId
                }
                transaction.update(userRef, "favorites", newFavorites)
                newFavorites.contains(productId)
            }.await()
        } catch (e: Exception) {
            Log.e("Dani", "Error al alternar favorito: ${e.message}")
            false
        }
    }

    suspend fun getUserCategoryClickRanking(): List<String> {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId.isNullOrEmpty()) {
            Log.e("UserRepository", "No authenticated user found.")
            return emptyList()
        }

        return try {
            val snapshot = db.collection("users").document(userId).get().await()
            val categoryClicks = snapshot.get("categoryClicks") as? Map<String, Long> ?: emptyMap()

            if (categoryClicks.isEmpty()) {
                defaultCategories
            } else {
                val sortedByClicks = categoryClicks.entries.sortedByDescending { it.value }.map { it.key }
                val remaining = defaultCategories.filterNot { it in sortedByClicks }
                sortedByClicks + remaining
            }

        } catch (e: Exception) {
            Log.e("UserRepository", "Error getting categoryClicks: ${e.message}")
            emptyList()
        }
    }

    suspend fun updateUserCategoryClick(category: String) {
        val user = auth.currentUser ?: return
        val userDocRef = db.collection("users").document(user.uid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userDocRef)

            val categoryClicks = snapshot.get("categoryClicks") as? Map<String, Long> ?: emptyMap()
            val mutableClicks = categoryClicks.toMutableMap()
            val currentValue = mutableClicks[category] ?: 0L
            mutableClicks[category] = currentValue + 1

            transaction.update(userDocRef, "categoryClicks", mutableClicks)
        }.await()
    }


}


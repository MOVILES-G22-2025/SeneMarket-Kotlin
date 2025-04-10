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

            categoryClicks.entries
                .sortedByDescending { it.value }
                .map { it.key }

        } catch (e: Exception) {
            Log.e("UserRepository", "Error getting categoryClicks: ${e.message}")
            emptyList()
        }
    }

}


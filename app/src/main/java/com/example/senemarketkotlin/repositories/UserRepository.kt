package com.example.senemarketkotlin.repositories

import com.example.senemarketkotlin.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
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
            fullName = fullName,
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

}


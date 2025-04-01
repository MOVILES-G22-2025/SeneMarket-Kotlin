package com.example.senemarketkotlin.repositories

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.Date

class StorageRepository() {
    suspend fun uploadImage(uri: Uri): String {
        return try {
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("product_images/${Date().time}")

            // Subir la imagen y esperar a que termine
            val uploadTaskSnapshot = imageRef.putFile(uri).await()

            // Obtener la URL de descarga
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            throw e // Relanza la excepción para que el llamador la maneje
        }
    }

}
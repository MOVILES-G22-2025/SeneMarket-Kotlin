package com.example.senemarketkotlin.repositories


import android.content.SharedPreferences
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
import kotlinx.serialization.ExperimentalSerializationApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class ProductRepository(private val db: FirebaseFirestore, private val auth: FirebaseAuth, private val sharedPreferences: SharedPreferences) {

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

    suspend fun getAllProducts(): List<ProductModel> = withContext(Dispatchers.IO) {
        try {
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
                    Log.e("ProductRepository", "Error deserializando producto: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error obteniendo productos: ${e.message}")
            emptyList()
        }
    }

    suspend fun getAllFavoritesProducts(): List<ProductModel> {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId.isNullOrEmpty()) {
            Log.e("UserRepository", "No authenticated user found.")
            return emptyList()
        }

        return try {
            val userSnapshot = db.collection("users").document(userId).get().await()
            val favoriteIds = userSnapshot.get("favorites") as? List<String> ?: emptyList()

            if (favoriteIds.isEmpty()) {
                return emptyList()
            }

            val favoriteProducts = mutableListOf<ProductModel>()

            for (id in favoriteIds) {
                try {
                    val productDoc = db.collection("products").document(id).get().await()
                    if (productDoc.exists()) {
                        val product = productDoc.toObject(ProductModel::class.java)
                        product?.id = productDoc.id

                        product?.price = when (val rawPrice = productDoc.get("price")) {
                            is Number -> rawPrice.toInt()
                            is String -> rawPrice.toIntOrNull() ?: 0
                            else -> 0
                        }

                        product?.let { favoriteProducts.add(it) }
                    }
                } catch (e: Exception) {
                    Log.e("ProductRepository", "Error getting product $id: ${e.message}")
                }
            }

            return favoriteProducts

        } catch (e: Exception) {
            Log.e("ProductRepository", "Error getting favorites: ${e.message}")
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

    suspend fun searchProducts(query: String): List<ProductModel> = withContext(Dispatchers.IO) {
        try {
            if (query.isBlank()) {
                return@withContext getAllProducts()
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
            Log.e("ProductRepository", "Error en búsqueda de productos: ${e.message}")
            emptyList()
        }
    }

    suspend fun searchFavoritesProducts(query: String): List<ProductModel> {
        return try {
            val favorites = getAllFavoritesProducts()

            if (query.isBlank()) {
                return favorites
            }

            // Filtrar por nombre, categoría o descripción si quieres hacerlo más completo
            favorites.filter {
                it.name?.contains(query, ignoreCase = true) ?: false
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error en búsqueda de productos favoritos: ${e.message}")
            emptyList()
        }
    }


    suspend fun getProductsByCategories(categories: List<String>): List<ProductModel> = withContext(Dispatchers.IO) {
         try {
            if (categories.isEmpty()) return@withContext getAllProducts()

            val snapshot = db.collection("products")
                .whereIn("category", categories)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ProductModel::class.java)?.apply {
                    id = doc.id
                    price = when (val rawPrice = doc.get("price")) {
                        is Number -> rawPrice.toInt()
                        is String -> rawPrice.toIntOrNull() ?: 0
                        else -> 0
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error filtrando productos por categorías: ${e.message}")
            emptyList()
        }
    }

    suspend fun existsDraftProduct(): Boolean {
        return sharedPreferences.contains("draftProduct")
    }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun getDraftProduct(): ProductModel {
        val raw = sharedPreferences.getString("draftProduct", "{}")
        val product = Json.decodeFromString<ProductModel>(raw?: "{}")
        return product
    }

    suspend fun clearDraftProduct() {
        with (sharedPreferences.edit()) {
            remove("draftProduct")
            apply()
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun saveDraftProduct(product: ProductModel) {
        val json = Json.encodeToString(product)
        with (sharedPreferences.edit()) {
            putString("draftProduct", json)
            apply()
        }
    }
}


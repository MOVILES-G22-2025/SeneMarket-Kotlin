package com.example.senemarketkotlin.models


import android.net.Uri
import com.example.senemarketkotlin.repositories.ProductRepository
import com.example.senemarketkotlin.repositories.StorageRepository

import android.util.Log

import com.example.senemarketkotlin.repositories.UserRepository

class DataLayerFacade (

    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository,
    private val productRepository: ProductRepository

){
    suspend fun login(email: String, password: String) {
        return userRepository.login(email, password)
    }

    suspend fun signUp(email: String, password: String,  fullName: String, career: String, semester: String) {
        return userRepository.signUp(email, password, fullName, career, semester)
    }

    suspend fun getAllProducts(): List<ProductModel> {
        val result = productRepository.getAllProducts()
        Log.d("Firestore", "Productos obtenidos Facade: $result")
        return result
    }

    suspend fun addProduct(product: ProductModel) {
        return productRepository.addProduct(product)

    }

    suspend fun uploadImage(uri: Uri): String {
        return storageRepository.uploadImage(uri)
    }

    suspend fun getProducts(): List<ProductModel> {
        val result = productRepository.getAllProducts()
        Log.d("Firestore", "Productos obtenidos Facade: $result")
        return result
    }

    suspend fun getFilteredProducts(query: String): List<ProductModel> {
        return productRepository.searchProducts(query)

    }

    suspend fun getProductById(productId: String): ProductModel? {
        return productRepository.getProductById(productId)
    }

    suspend fun getProductsByCategories(categories: List<String>): List<ProductModel> {
        return productRepository.getProductsByCategories(categories)
    }

    suspend fun findUserById(userId: String): UserModel? {
        return userRepository.findUserById(userId)
    }

    suspend fun getUserCategoryClickRanking(): List<String> {
        return userRepository.getUserCategoryClickRanking()
    }
}
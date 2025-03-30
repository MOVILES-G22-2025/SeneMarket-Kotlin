package com.example.senemarketkotlin.models

import com.example.senemarketkotlin.repositories.ProductRepository
import com.example.senemarketkotlin.repositories.UserRepository

class DataLayerFacade (

    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
){
    suspend fun login(email: String, password: String) {
        return userRepository.login(email, password)
    }

    suspend fun signUp(email: String, password: String,  fullName: String, career: String, semester: String) {
        return userRepository.signUp(email, password, fullName, career, semester)
    }

    suspend fun getProducts(): List<ProductModel> {
        return productRepository.getAllProducts()
    }

}
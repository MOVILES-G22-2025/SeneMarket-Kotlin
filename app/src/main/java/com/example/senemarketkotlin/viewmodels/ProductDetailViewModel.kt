package com.example.senemarketkotlin.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.models.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val dataLayerFacade: DataLayerFacade,
    private val productId: String
) : ViewModel() {

    private val _product = MutableStateFlow<ProductModel?>(null)
    val product: StateFlow<ProductModel?> = _product
    private val _userName = MutableStateFlow<String>("Cargando...")
    val userName: StateFlow<String> = _userName.asStateFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            try {
                _product.value = dataLayerFacade.getProductById(productId)
            } catch (e: Exception) {
                Log.e("Dani", "Error cargando producto: ${e.message}")
            }
        }
    }

    fun findUserById(userId: String) {
        viewModelScope.launch {
            try {
                val user = dataLayerFacade.findUserById(userId)
                _userName.value = user?.name ?: "Usuario no encontrado"
                Log.d("Dani", "Usuario encontrado: ${_userName.value}")
            } catch (e: Exception) {
                _userName.value = "Error al obtener usuario"
                Log.e("Dani", "Error buscando usuario: ${e.message}")
            }
        }
    }

    class Factory(
        private val dataLayerFacade: DataLayerFacade,
        private val productId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductDetailViewModel(dataLayerFacade, productId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

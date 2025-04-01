package com.example.senemarketkotlin.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.models.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.debounce
import kotlinx.coroutines.withContext

class HomeScreenViewModel(
    private val dataLayerFacade: DataLayerFacade
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    private val _searchQuery = MutableStateFlow("")
    private val _filteredProducts = MutableStateFlow<List<ProductModel>>(emptyList())
    val filteredProducts: StateFlow<List<ProductModel>> = _filteredProducts

    init {
        getProducts()
        viewModelScope.launch {
            _searchQuery.debounce(300).collectLatest { query ->
                _filteredProducts.value = if (query.isBlank()) {
                    _products.value
                } else {
                    dataLayerFacade.getFilteredProducts(query)
                }
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            val result = dataLayerFacade.getFilteredProducts("")
            _products.value = result
            _filteredProducts.value = result
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun filterProducts(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                _filteredProducts.value = _products.value // Si no hay búsqueda, muestra todos
            } else {
                val filteredList = dataLayerFacade.getFilteredProducts(query) // Búsqueda en Firebase
                _filteredProducts.value = filteredList
            }
        }
    }

    class Factory(
        private val dataLayerFacade: DataLayerFacade,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeScreenViewModel(dataLayerFacade) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}
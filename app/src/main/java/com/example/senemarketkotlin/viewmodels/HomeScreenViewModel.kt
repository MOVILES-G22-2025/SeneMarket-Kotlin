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

    private val _categoryRanking = MutableStateFlow<List<String>>(emptyList())
    val categoryRanking: StateFlow<List<String>> = _categoryRanking
    private val _selectedCategories = MutableStateFlow<List<String>>(emptyList())


    private val _searchQuery = MutableStateFlow("")
    private val _filteredProducts = MutableStateFlow<List<ProductModel>>(emptyList())
    val filteredProducts: StateFlow<List<ProductModel>> = _filteredProducts

    init {
        getProducts()
        getUserCategoryRanking()

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

    private fun getUserCategoryRanking() {
        viewModelScope.launch {
            val ranking = dataLayerFacade.getUserCategoryClickRanking()
            _categoryRanking.value = ranking
        }
    }

    fun filterBySelectedCategories(categories: List<String>) {
        viewModelScope.launch {
            val allProducts = _products.value

            val categoryFiltered = if (categories.isEmpty()) {
                allProducts
            } else {
                allProducts.filter { product -> product.category in categories }
            }

            val query = _searchQuery.value

            _filteredProducts.value = if (query.isBlank()) {
                categoryFiltered.sortedByDescending { it.timestamp }
            } else {
                categoryFiltered.filter { product ->
                    product.name?.contains(query, ignoreCase = true) == true
                }.sortedByDescending { it.timestamp }
            }
        }
    }

    fun toggleCategoryFilter(category: String) {
        val current = _selectedCategories.value.toMutableList()
        if (current.contains(category)) {
            current.remove(category)
        } else {
            current.add(category)
        }
        _selectedCategories.value = current
        filterBySelectedCategories(current)
    }

    fun registerCategoryClick(category: String) {
        viewModelScope.launch {
            try {
                dataLayerFacade.updateUserCategoryClick(category)
                getUserCategoryRanking()
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Error updating category click", e)
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
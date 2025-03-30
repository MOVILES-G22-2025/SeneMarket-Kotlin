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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenViewModel(
    private val dataLayerFacade: DataLayerFacade
) : ViewModel() {

    private val _product: MutableStateFlow<List<ProductModel>> =
        MutableStateFlow<List<ProductModel>>(emptyList())
    val product: StateFlow<List<ProductModel>> = _product

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            val result: List<ProductModel> = withContext(Dispatchers.IO) {
                dataLayerFacade.getProducts()
            }
            Log.d("Firestore", "Productos obtenidos: $result")
            _product.value = result
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
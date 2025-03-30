package com.example.senemarketkotlin.viewmodels

import androidx.lifecycle.ViewModel
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
    private val dataLayerFacade: DataLayerFacade,
    private val navController: NavController
) : ViewModel() {

    private val _product: MutableStateFlow<List<ProductModel>> =
        MutableStateFlow<List<ProductModel>>(emptyList())
    val product: StateFlow<List<ProductModel>> = _product

    init {
        getProducts()
    }

    fun goToShoppingCart() {
        navController.navigate("shoppingcart")
    }

    private fun getProducts() {
        viewModelScope.launch {
            val result: List<ProductModel> = withContext(Dispatchers.IO) {
                dataLayerFacade.getProducts()
            }
            _product.value = result
        }
    }

}
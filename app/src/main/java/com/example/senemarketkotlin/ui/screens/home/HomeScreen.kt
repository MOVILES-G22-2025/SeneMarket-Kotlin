package com.example.senemarketkotlin.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.models.ProductModel
import com.example.senemarketkotlin.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(dataLayerFacade: DataLayerFacade, navController: NavController) {

    val homeScreenViewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.Factory(dataLayerFacade))
    val products by homeScreenViewModel.products.collectAsState(initial = emptyList())
    //searchBar(navController)
    HomeScreenProducts(products)
}

/*
@Composable
fun searchBar(navController: NavController) {
    Column {
        Button(
            onClick = { navController.navigate("") },
            modifier = TODO(),
            enabled = TODO(),
            shape = TODO(),
            colors = TODO(),
            elevation = TODO(),
            border = TODO(),
            contentPadding = TODO(),
            interactionSource = TODO(),
            content = TODO()
        )
    }
}
 */

@Composable
fun HomeScreenProducts(products: List<ProductModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(product: ProductModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            model = product.imagePortada,
            contentDescription = "Product image"
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = product.name.orEmpty(), color = Color.Black)
        Text(text = "$" + product.price.toString(), color = Color.Black)
    }
}

@Preview
@Composable
fun ProductItemPreview() {
    val product = ProductModel(
        category = "Transportation",
        description = "333",
        favoritedBy = listOf("KkLW90xOWqURCkjohEZn6ImrCV32"),
        imagePortada = "https://firebasestorage.googleapis.com/v0/b/senemarket-app.firebasestorage.app/o/product_images%2F1742477547599?alt=media&token=b6d34325-3849-4cd8-9cc9-1ffa267f5cdd",
        imageUrls = listOf(
            "https://firebasestorage.googleapis.com/v0/b/senemarket-app.firebasestorage.app/o/product_images%2F1742558117414?alt=media&token=f5d1d10a-45e5-4d3c-a7bc-97f913fe380a",
            "https://firebasestorage.googleapis.com/v0/b/senemarket-app.firebasestorage.app/o/product_images%2F1742558117414?alt=media&token=f5d1d10a-45e5-4d3c-a7bc-97f913fe380a",
            "https://firebasestorage.googleapis.com/v0/b/senemarket-app.firebasestorage.app/o/product_images%2F1742558120171?alt=media&token=c4b6baae-3708-4aa7-95a9-37208cb6642c",
            "https://firebasestorage.googleapis.com/v0/b/senemarket-app.firebasestorage.app/o/product_images%2F1742558122173?alt=media&token=51f0d7b9-47b4-4234-985a-3cba37dd265a",
            "https://firebasestorage.googleapis.com/v0/b/senemarket-app.firebasestorage.app/o/product_images%2F1742558123836?alt=media&token=fd44729f-075f-44c7-b2cc-866b1f707f0d"
        ),
        name = "varia",
        price = "12354".toDouble(),
        sellerName = "jcedielb",
        timestamp = "21 de marzo de 2025, 6:55:27a.m. UTC-5",
        userId = "KkLW90xOWqURCkjohEZn6ImrCV32"
    )

    ProductItem(product)
}
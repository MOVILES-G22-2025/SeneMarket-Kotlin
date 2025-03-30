package com.example.senemarketkotlin.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.models.ProductModel
import com.example.senemarketkotlin.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(dataLayerFacade: DataLayerFacade, navController: NavController) {

    val homeScreenViewModel: HomeScreenViewModel =
        viewModel(factory = HomeScreenViewModel.Factory(dataLayerFacade))
    val products by homeScreenViewModel.products.collectAsState()

    LaunchedEffect(Unit) {
        homeScreenViewModel.getProducts()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        HomeScreenProducts(products)
    }
}

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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = product.imagePortada,
                contentDescription = "Product Image",
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.name.orEmpty(),
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Text(
                text = "$${product.price}",
                color = Color(0xFF4CAF50),
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
        }
    }
}
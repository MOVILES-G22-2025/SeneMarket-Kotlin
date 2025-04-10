package com.example.senemarketkotlin.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.util.query
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.senemarketkotlin.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.models.ProductModel
import com.example.senemarketkotlin.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(dataLayerFacade: DataLayerFacade, navController: NavController) {

    val homeScreenViewModel: HomeScreenViewModel =
        viewModel(factory = HomeScreenViewModel.Factory(dataLayerFacade))
    val filteredProducts by homeScreenViewModel.filteredProducts.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val categoryRanking by homeScreenViewModel.categoryRanking.collectAsState()

    val selectedFilters = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        homeScreenViewModel.getProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Fila superior fija
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(16.dp)
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Home",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif
            )
        }

        SearchBar(
            searchQuery,
            onQueryChange = {
                searchQuery = it
                homeScreenViewModel.onSearchQueryChanged(it.text)
            }
        )

        if (categoryRanking.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter_list),
                    contentDescription = "Filter icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categoryRanking) { category ->
                        FilterChip(
                            label = category,
                            selected = selectedFilters.contains(category),
                            onClick = {
                                if (selectedFilters.contains(category)) {
                                    selectedFilters.remove(category)
                                } else {
                                    selectedFilters.add(category)
                                }
                                homeScreenViewModel.filterBySelectedCategories(selectedFilters)
                            }
                        )
                    }
                }
            }

        }

        HomeScreenProducts(filteredProducts, navController, homeScreenViewModel)
    }
}

@Composable
fun SearchBar(
    searchQuery: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { onQueryChange(it) },
            placeholder = { Text("Search products...") },
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search icon")
            },
            modifier = Modifier.weight(1f).clip(RoundedCornerShape(30.dp)),
            shape = RoundedCornerShape(30.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { /* TODO: Navigate to cart */ }) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = "Shopping cart icon",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = Color.Black
    val checkColor = Color(0xFFFFC928)
    val backgroundColor = Color.White

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .border(2.dp, checkColor, CircleShape)
                .background(
                    if (selected) checkColor else Color.Transparent,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}


@Composable
fun HomeScreenProducts(products: List<ProductModel>, navController: NavController, viewModel: HomeScreenViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product, navController, viewModel)
        }
    }
}

@Composable
fun ProductItem(product: ProductModel, navController: NavController, viewModel: HomeScreenViewModel) {
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp)
            .padding(8.dp)
            .clickable {
                product.category?.let { category ->
                    viewModel.registerCategoryClick(category)
                }
                navController.navigate("productDetail/${product.id}")
            },
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
                model = product.imageUrls?.firstOrNull() ?: product.imagePortada,
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.name.orEmpty(),
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$${product.price}",
                color = Color(0xFF4CAF50),
                fontSize = 14.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
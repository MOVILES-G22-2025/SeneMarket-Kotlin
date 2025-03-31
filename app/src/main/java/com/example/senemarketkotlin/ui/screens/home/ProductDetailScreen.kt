package com.example.senemarketkotlin.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.models.ProductModel
import com.example.senemarketkotlin.viewmodels.ProductDetailViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(productId: String, dataLayerFacade: DataLayerFacade, navController: NavController) {
    val viewModel: ProductDetailViewModel = viewModel(factory = ProductDetailViewModel.Factory(dataLayerFacade, productId))
    val product by viewModel.product.collectAsState()

    if (product == null) {
        // Muestra un indicador de carga mientras se obtiene el producto
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val images = remember(product) {
        listOfNotNull(product?.imagePortada) + (product?.imageUrls.orEmpty())
    }
    var selectedImage by remember { mutableStateOf(images.firstOrNull() ?: "") }
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Fila superior con botón de atrás y título centrado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = product?.name.orEmpty(),
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif
            )

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        // Imagen de portada o imagen seleccionada del carrusel
        AsyncImage(
            model = selectedImage,
            contentDescription = "Product Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Carrusel de imágenes si hay más de una
        if (images.size > 1) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(images) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Thumbnail",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedImage = imageUrl },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre con estrella clickable
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "$${product?.price.toString()}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { isFavorite = !isFavorite }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Favorite",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(text = "Category", fontSize = 15.sp, color = Color.Black, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold)
        Text(text = product?.category.orEmpty(), fontSize = 14.sp, color = Color.Black, fontFamily = FontFamily.SansSerif)

        Spacer(modifier = Modifier.height(16.dp))

        // Información del vendedor
        Text(text = "Sold by", fontSize = 15.sp, color = Color.Black, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold)
        Text(text = product?.userId.orEmpty(), fontSize = 14.sp, fontFamily = FontFamily.SansSerif)

        Spacer(modifier = Modifier.height(16.dp))

        // Descripción del producto
        Text(text = "Description", fontSize = 15.sp, color = Color.Black, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold)
        Text(text = product?.description.orEmpty(), fontSize = 14.sp)

        Spacer(modifier = Modifier.weight(1f))

        // Botones de compra y contactar al vendedor
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* Comprar */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC928)),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Buy now", color = Color.Black, fontFamily = FontFamily.SansSerif)
                }
                Button(
                    onClick = { /* TODO: Agregar al carrito */ },
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC928))
                ) {
                    Text(text = "Add to cart", color = Color.Black, fontFamily = FontFamily.SansSerif)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* TODO: Contactar vendedor */ },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC928)),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally) // Centra el botón
            ) {
                Text(text = "Talk with the seller", color = Color.Black, fontFamily = FontFamily.SansSerif)
            }
        }
    }
}
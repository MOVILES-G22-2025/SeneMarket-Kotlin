package com.example.senemarketkotlin.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.senemarketkotlin.R
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.models.ProductModel
import com.example.senemarketkotlin.models.UserModel
import com.example.senemarketkotlin.viewmodels.ProductDetailViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    productId: String,
    fromScreen: String,
    dataLayerFacade: DataLayerFacade,
    navController: NavController
) {
    val viewModel: ProductDetailViewModel =
        viewModel(factory = ProductDetailViewModel.Factory(dataLayerFacade, productId))
    val product by viewModel.product.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(product?.userId) {
        product?.userId?.let { userId ->
            viewModel.findUserById(userId)
        }
    }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val images = product?.imageUrls.orEmpty()
    var selectedImageIndex by remember { mutableStateOf(0) }

    Spacer(modifier = Modifier.height(16.dp))

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        // Fila superior fija con botón de atrás y título
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF2F1F7))
                .padding(16.dp)
                .statusBarsPadding(),
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
                onClick = {
                    if (fromScreen == "favorites") {
                        navController.navigate("favorites") {
                            popUpTo("favorites") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }

        // Contenido con scroll
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Imagen principal con navegación
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                IconButton(
                    onClick = { if (selectedImageIndex > 0) selectedImageIndex-- },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous Image"
                    )
                }

                AsyncImage(
                    model = images.getOrNull(selectedImageIndex),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { if (selectedImageIndex < images.size - 1) selectedImageIndex++ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next Image"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Precio con corazon clickable
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
                IconButton(onClick = { viewModel.toggleFavorite() }) {
                    Icon(
                        painter = if (isFavorite) painterResource(R.drawable.ic_yellow_heart_filled) else painterResource(
                            R.drawable.ic_yellow_heart_outlined
                        ),
                        contentDescription = "Favorite",
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFFFFC928)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar la categoría
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Category: ", fontSize = 20.sp, color = Color.Black, fontFamily = FontFamily.SansSerif)
                Text(text = product?.category.orEmpty(), fontSize = 20.sp, color = Color.Black, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Información del vendedor con botón de chat
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Sold by", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = userName, fontSize = 18.sp, color = Color.Gray)
                }
                Button(
                    onClick = { /* TODO: Chat con el vendedor */ },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC928))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chat_bubble_outline),
                        contentDescription = "Chat Icon",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Chat", fontSize = 18.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(text = "Description", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = product?.description.orEmpty(), fontSize = 18.sp)
        }

        // Botones de compra y contactar al vendedor
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF2F1F7))
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* TODO: Comprar */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC928)),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                ) {
                    Text(text = "Buy now", fontSize = 18.sp, color = Color.Black, fontFamily = FontFamily.SansSerif)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

    }

}


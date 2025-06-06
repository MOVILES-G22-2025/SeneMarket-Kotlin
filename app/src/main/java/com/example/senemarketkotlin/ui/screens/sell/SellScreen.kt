package com.example.senemarketkotlin.ui.screens.sell

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.ImageButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.senemarketkotlin.R
import com.example.senemarketkotlin.ui.theme.White
import com.example.senemarketkotlin.ui.theme.Yellow30
import com.example.senemarketkotlin.utils.Intent
import com.example.senemarketkotlin.viewmodels.LoginScreenViewModel
import com.example.senemarketkotlin.viewmodels.SellScreenViewModel
import com.google.firebase.Timestamp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellScreen (viewModel: SellScreenViewModel) {
    val currentContext = LocalContext.current
    val isCreating: Boolean by viewModel.isCreating.observeAsState(initial = false)
    val isOffline: Boolean by viewModel.isOffline.observeAsState(initial = false)

    // launches photo picker
    val pickImageFromAlbumLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        //viewModel.onReceive(Intent.OnFinishPickingImagesWith(currentContext, urls))

        viewModel.onImageUrlChange(uri ?: Uri.EMPTY)
    }

    val file  = currentContext.createImageFile()
    val uri = FileProvider.getUriForFile(Objects.requireNonNull(currentContext), currentContext.packageName + ".provider", file)

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        viewModel.onImageUrlChange(uri)
    }


    // launches camera permissions
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
        if (permissionGranted) {
            //viewModel.onReceive(Intent.OnPermissionGrantedWith(currentContext))
            cameraLauncher.launch(uri)
        } else {
            // handle permission denied such as:
            viewModel.onReceive(Intent.OnPermissionDenied)
        }
    }

    viewModel.checkDraftProduct()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        // Back arrow
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { viewModel.goToHome()}
            )
        }

        Text("Add product", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))


        val modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

        var expanded by remember { mutableStateOf(false) }

        val nameProduct: String by viewModel.nameProduct.observeAsState(initial = "")
        val description: String by viewModel.description.observeAsState(initial = "")
        val category: String by viewModel.category.observeAsState(initial = "")
        val imageUri: Uri? by viewModel.imageUrl.observeAsState(initial = Uri.EMPTY)

        val price: Int by viewModel.price.observeAsState(initial = 0)
        val error: String by viewModel.error.observeAsState(initial = "")

        Spacer(modifier = Modifier.height(10.dp))

        Row(


           horizontalArrangement = Arrangement.spacedBy(16.dp),
           verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                modifier = Modifier.padding(5.dp),
                shape = RoundedCornerShape(50),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camara),
                    contentDescription = "Camera",
                    modifier = Modifier.size(20.dp) // Ajusta el tamaño del ícono
                )
                Text(text = "Take photo", modifier = Modifier.padding(start = 8.dp))
            }

            OutlinedButton(
                onClick = {
                    val mediaRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    pickImageFromAlbumLauncher.launch(mediaRequest)
                },
                modifier = Modifier.padding(5.dp),
                shape = RoundedCornerShape(50),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Image(
                    painter = painterResource(id = R.drawable.upload),
                    contentDescription = "Upload",
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Upload images", modifier = Modifier.padding(start = 8.dp))
            }
        }

        if (imageUri?.path?.isNotEmpty() == true)
            Column {
                Text(text = "Selected Pictures:", color = Color.Gray,textAlign = TextAlign.Center)
                AsyncImage(modifier = Modifier.padding(4.dp).width(120.dp)  // Ancho específico
                    .height(200.dp),
                    model = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
                Button(
                    onClick = {
                        viewModel.onImageUrlChange(Uri.EMPTY)
                    }, modifier = Modifier
                        //.fillMaxWidth()
                        .padding(5.dp)
                        .align(Alignment.CenterHorizontally), colors = ButtonDefaults.buttonColors(containerColor = Yellow30)
                ) {
                    Text(text = "Remove image", color = White)
                }

            }
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nameProduct,
            onValueChange = { viewModel.onNameProductChange(it) },
            label = { Text("Name") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107),
                unfocusedBorderColor = Color.Black
            )

        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = { Text("Description") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107),
                unfocusedBorderColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(10.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                label = { Text("Category") },
                modifier = modifier.menuAnchor(),
                shape = RoundedCornerShape(8.dp),
                readOnly = true, // Para evitar que el usuario escriba
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown",
                        modifier = Modifier.clickable { expanded = true }
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFC107),
                    unfocusedBorderColor = Color.Black
                )
            )


            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                viewModel.categories.forEach { selection ->
                    DropdownMenuItem(
                        text = { Text(selection) },
                        onClick = {
                            viewModel.onCategoryChange(selection)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = price.toString(),
            onValueChange = { newValue ->
                val sanitizedValue = newValue.toIntOrNull() ?: 0
                viewModel.onPriceChange(sanitizedValue)
            },
            label = { Text("Price") },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107),
                unfocusedBorderColor = Color.Black
            )
        )

        if (error.isNotEmpty())
            Text("$error", color = Color.Red)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {viewModel.add()}, enabled = !isCreating, modifier = Modifier
                //.fillMaxWidth()
                .padding(5.dp), colors = ButtonDefaults.buttonColors(containerColor = Yellow30)
        ) {
            if (isCreating) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                    strokeWidth = 2.dp
                )
                Text(text = "Creating...", color = White)
            } else {
                Text(text = "Add", color = White)
            }
        }


        Spacer(modifier = Modifier.weight(2f))

        Spacer(modifier = Modifier.height(100.dp))
    }
    OfflineDraftPopup(
        isOffline = isOffline,
        onSaveDraft = {
            // Lógica para guardar borrador
            viewModel.saveDraftProduct()
        },
        onDismiss = {
        }
    )
}

@Composable
fun OfflineDraftPopup(
    isOffline: Boolean,
    onSaveDraft: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isOffline) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "No internet connection")
            },
            text = {
                Text("Your product will be saved as a draft. Revisit this screen later to view the draft.")
            },
            confirmButton = {
                Button(onClick = onSaveDraft) {
                    Text("Save draft")
                }
            }
        )
    }
}


        // Title
fun Context.createImageFile(): File {
    val timestamp = SimpleDateFormat("yyyy_MM_dd_hh:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timestamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}
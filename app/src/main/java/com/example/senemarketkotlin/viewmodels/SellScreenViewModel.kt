package com.example.senemarketkotlin.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.senemarketkotlin.models.DataLayerFacade
import com.example.senemarketkotlin.models.ProductModel
import com.example.senemarketkotlin.utils.Intent
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.coroutineContext

class SellScreenViewModel (
    private val navController: NavController,
    private val dataLayerFacade: DataLayerFacade,
    private val context: Context
): ViewModel() {


    private val _nameProduct = MutableLiveData<String>()
    val nameProduct: LiveData<String> = _nameProduct

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> = _category

    private val _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price

    private val _imageUrl = MutableLiveData<Uri?>()
    val imageUrl: LiveData<Uri?> = _imageUrl

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    var selectedPictures: List<Uri> = emptyList()


    val categories = listOf(
        "Academic materials", "Technology and electronics", "Transportation",
        "Clothing and accessories", "Housing", "Entertainment", "Sports and fitness"
    )

    fun add() {
        viewModelScope.launch {
            try {


                val nameProduct = _nameProduct.value.orEmpty()
                val description = _description.value.orEmpty()
                val category = _category.value.orEmpty()
                val imageUrl = _imageUrl.value
                val price = _price.value ?: 0

                if (imageUrl?.path?.isNotEmpty() != true) {
                    throw Exception("Seleccione una imagen")
                }

                // Guardar imagen en storage
                val imageUrlInFirebase = dataLayerFacade.uploadImage(imageUrl)


                // Guardar producto en database
                dataLayerFacade.addProduct(product = ProductModel(
                    name = nameProduct,
                    description = description,
                    category = category,
                    price = price,
                    imageUrls = listOf(imageUrlInFirebase),
                    imagePortada = imageUrlInFirebase
                ))
                navController.navigate ("home")


            } catch (e: Exception) {

                _error.value = e.message

            }
        }

    }


    fun onReceive(intent: Intent) = viewModelScope.launch {
        when(intent) {
            is Intent.OnPermissionGrantedWith -> {
                // Create an empty image file in the app's cache directory
                val tempFile = File.createTempFile(
                    "temp_image_file_", /* prefix */
                    ".jpg", /* suffix */
                    intent.compositionContext.cacheDir  /* cache directory */
                )

                // Create sandboxed url for this temp file - needed for the camera API
                val uri = FileProvider.getUriForFile(intent.compositionContext,
                    "${context.packageName}.provider", /* needs to match the provider information in the manifest */
                    tempFile
                )
                _imageUrl.value = uri
                selectedPictures = listOf(uri)
            }

            is Intent.OnPermissionDenied -> {
                // maybe log the permission denial event
                println("User did not grant permission to use the camera")
            }

            is Intent.OnFinishPickingImagesWith -> {
                if (intent.imageUrls.isNotEmpty()) {
                    // Handle picked images
                    val newImages = mutableListOf<Uri>()
                    for (eachImageUrl in intent.imageUrls) {

                        newImages.add(eachImageUrl)
                    }

                    _imageUrl.value = null
                    selectedPictures = (selectedPictures + newImages)
                } else {
                    // user did not pick anything
                }
            }

            is Intent.OnImageSavedWith -> {
                val tempImageUrl = _imageUrl.value

                Log.d("CAMERA", "IMAGE URIIII: ${_imageUrl.value.toString()}")

                if (tempImageUrl != null) {
                    val source = ImageDecoder.createSource(intent.compositionContext.contentResolver, tempImageUrl)

                    val currentPictures = selectedPictures.toMutableList()
                    currentPictures.add(tempImageUrl)

                    _imageUrl.value = null
                    selectedPictures = currentPictures

                }
            }

            is Intent.OnImageSavingCanceled -> {
                _imageUrl.value = null
            }
        }
    }


    fun onNameProductChange(nameProduct: String) {
        _nameProduct.value = nameProduct

    }

    fun onDescriptionChange(description: String) {
        _description.value = description
    }

    fun onCategoryChange(category: String) {
        _category.value = category
    }

    fun onPriceChange(price: Int) {
        _price.value = price
    }

    fun onImageUrlChange(uri: Uri) {
        _imageUrl.value = uri
    }

    fun createFile(context: Context)
    {
        val tempFile = File.createTempFile(
            "temp_image_file_", /* prefix */
            ".jpg", /* suffix */
            context.cacheDir  /* cache directory */
        )

        // Create sandboxed url for this temp file - needed for the camera API
        val uri = FileProvider.getUriForFile(context,
            "${context.packageName}.provider", /* needs to match the provider information in the manifest */
            tempFile
        )
        _imageUrl.value = uri
    }

    fun goToHome(){
        this.navController.navigate("home")
    }

}
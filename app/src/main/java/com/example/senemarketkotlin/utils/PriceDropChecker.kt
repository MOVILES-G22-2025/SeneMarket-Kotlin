package com.example.senemarketkotlin.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.senemarketkotlin.R
import com.example.senemarketkotlin.models.ProductModel

object PriceDropChecker {

    private const val PREF_NAME = "product_prices"
    private const val CHANNEL_ID = "price_drop_channel"
    private const val CHANNEL_NAME = "Price Drop Alerts"

    // Variable que se debe asignar desde Application
    var appContext: Context? = null

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun checkPriceDrops(favorites: List<ProductModel>) {
        val context = appContext ?: return
        val sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        createNotificationChannel(context)

        for (product in favorites) {
            val productId = product.id ?: continue
            val previousPrice = sharedPrefs.getFloat(productId, Float.MAX_VALUE)
            val currentPrice = product.price?.toFloat() ?: continue

            if (currentPrice < previousPrice) {
                showNotification(context, product.name ?: "Producto", previousPrice, currentPrice)
            }

            sharedPrefs.edit().putFloat(productId, currentPrice).apply()
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(context: Context, productName: String, oldPrice: Float, newPrice: Float) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_price_drop)
            .setContentTitle("¡Lower price!")
            .setContentText("$productName lower price to $$newPrice")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(productName.hashCode(), notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones cuando un producto favorito baja de precio"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
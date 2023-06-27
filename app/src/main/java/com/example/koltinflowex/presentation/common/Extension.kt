package com.example.koltinflowex.presentation.common

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.onLoading(isLoading: Boolean) {
    if (isLoading) {

    }
}

fun Context.isNetworkAvailable(): Boolean {
    val result: Boolean
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

fun Activity.hideKeyBoard() {
    val manager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Context.loadImage(imageUrl: String?, imageView: ImageView, progressBar: ProgressBar) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            progressBar.visibility = ProgressBar.VISIBLE // Show the progress bar

            withContext(Dispatchers.IO) {
                val bitmap = Glide.with(this@loadImage).asBitmap().load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).submit().get()

                bitmap
            }.let { bitmap ->
                imageView.setImageBitmap(bitmap)
            }

            progressBar.visibility =
                ProgressBar.GONE // Hide the progress bar once the image is loaded
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the error, show a placeholder, or display an error message to the user.
            progressBar.visibility = ProgressBar.GONE // Hide the progress bar in case of error
        }
    }
}
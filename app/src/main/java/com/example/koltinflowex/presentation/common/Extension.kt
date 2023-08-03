package com.example.koltinflowex.presentation.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.koltinflowex.R
import javax.annotation.Nullable


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

fun Context.loadImage(imageUrl: String?, imageView: ImageView,progressBar: ProgressBar) {
    Glide.with(this).load(imageUrl)
        .placeholder(R.drawable.placeholder)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                @Nullable e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                imageView.setImageResource(R.drawable.placeholder)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                imageView.setImageDrawable(resource)
                return false
            }
        }).into(imageView)
}


fun <T> Activity.startNewActivity(s: Class<T>, killCurrent: Boolean = false) {
    val intent = Intent(this, s)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
    if (killCurrent) finish()
}
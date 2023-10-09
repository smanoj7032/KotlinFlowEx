package com.example.koltinflowex.presentation.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.text.InputFilter
import android.text.InputType
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.koltinflowex.R
import javax.annotation.Nullable


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.onLoading(isLoading: Boolean) {
    if (isLoading) {

    }
}

fun Context.performActionWithInternetCheck(action: () -> Unit) {
    if (isNetworkAvailable()) {
        action.invoke()
    } else {
        this.showToast("No internet connection available")
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

fun Context.loadImage(imageUrl: String?, imageView: ImageView) {
    Glide.with(this).load(imageUrl).placeholder(R.drawable.placeholder)
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


fun TextView.setSelectableTextColor() {
    setTextIsSelectable(true)

    val colorSpan = ForegroundColorSpan(resources.getColor(R.color.green))

    customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menu?.clear()
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            val text = text as? Spannable
            text?.let {
                val start = selectionStart
                val end = selectionEnd
                // Remove previous color span if it exists
                it.getSpans(0, it.length, ForegroundColorSpan::class.java)
                    .forEach { span -> it.removeSpan(span) }
                if (start != end) {
                    // Apply the new color span
                    it.setSpan(
                        colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
    }
}


private fun TextView.showColorInputDialog(isSuccess: (Boolean) -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Enter Color Code")

    val input = EditText(context)
    input.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    val maxLength = 6
    input.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    builder.setView(input)

    builder.setPositiveButton("OK") { _, _ ->
        val colorCode = "#${input.text}"
        Log.e("ColorCode--->>", "showColorInputDialog: $colorCode")
        // Handle the color code entered by the user and update the text color span
        updateTextColorSpan(colorCode) { isSuccess(it) }
    }
    builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
    builder.setOnDismissListener {
        isSuccess(true)
    }

    builder.show()
}

private fun TextView.updateTextColorSpan(color: String, isSuccess: (Boolean) -> Unit) = try {
    val text = text as? Spannable
    text?.let {
        val start = selectionStart
        val end = selectionEnd
        it.getSpans(0, it.length, ForegroundColorSpan::class.java)
            .forEach { span -> it.removeSpan(span) }

        if (start != end) {
            val colorSpan = ForegroundColorSpan(Color.parseColor(color))
            it.setSpan(
                colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            isSuccess(true)
        }
    }
} catch (_: Exception) {
}

fun TextView.setSelectableTextColor2() {
    setTextIsSelectable(true)
    customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menu?.clear()
            showColorInputDialog { mode?.finish() }
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
        }
    }
}

fun getVideoThumbnail(videoUri: Uri): Bitmap? {
    val thumbnailSize = ThumbnailUtils.OPTIONS_RECYCLE_INPUT
    val bitmap = ThumbnailUtils.createVideoThumbnail(videoUri.path!!, thumbnailSize)
    return if (bitmap != null) {
        Bitmap.createScaledBitmap(bitmap, 120, 120, true)
    } else {
        null
    }
}
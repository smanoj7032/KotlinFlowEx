package com.example.koltinflowex.presentation.common.loader

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.example.koltinflowex.R

class Loader(context: Context) {
    val lineScaleLoader: Dialog = Dialog(context)
    private lateinit var tvMessage: TextView

    init {
        lineScaleLoader.setContentView(R.layout.line_scale_loader)
        lineScaleLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width: Int = WindowManager.LayoutParams.MATCH_PARENT
        val height: Int = WindowManager.LayoutParams.MATCH_PARENT
        lineScaleLoader.window?.setLayout(width, height)
        lineScaleLoader.window?.setGravity(Gravity.CENTER)
        showMessage(null)
    }

    fun showMessage(message: String?) {
        tvMessage = lineScaleLoader.findViewById<TextView>(R.id.tv_message)
        if (message.isNullOrEmpty()) {
            tvMessage.visibility = View.GONE
        } else {
            tvMessage.visibility = View.VISIBLE
            tvMessage.text = message
        }
    }
}
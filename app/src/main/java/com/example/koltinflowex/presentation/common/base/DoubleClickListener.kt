package com.example.koltinflowex.presentation.common.base

import android.util.Log
import android.view.View

class DoubleClickListener(private val onSingleClick: () -> Unit) : View.OnClickListener {
    private var lastClickTime: Long = 0
    private val DOUBLE_CLICK_TIME_DELTA: Long = 300 // Adjust this value as needed

    override fun onClick(v: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            Log.e("TAG-->>", "onClick: DoubleClicked", )
        } else {
            // Single click detected
            onSingleClick.invoke()
        }
        lastClickTime = clickTime
    }
}

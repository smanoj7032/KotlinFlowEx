package com.example.koltinflowex.presentation.common.base

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View

class DoubleClickListener(context: Context?, private val onSingleClick: () -> Unit) :
    View.OnClickListener {
    private var lastClickTime: Long = 0
    private val DOUBLE_CLICK_TIME_DELTA: Long = 500 // Adjust this value as needed
    private val vibrator: Vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    override fun onClick(v: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            Log.e("TAG-->>", "onClick: DoubleClicked")
        } else {
            performHapticFeedback()
            onSingleClick.invoke()
        }
        lastClickTime = clickTime
    }

    private fun performHapticFeedback() {
        if (vibrator.hasVibrator()) {
            val effect = VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }
}

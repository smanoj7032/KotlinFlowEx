package com.example.koltinflowex.presentation.common.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.koltinflowex.R

object AlertManager {
    private const val ALERT_POSITIVE = 0
    private const val ALERT_NEGATIVE = 1
    private const val ALERT_NEUTRAL = 1

    fun showPositiveAlert(activity: Activity, message: String?) {
        showAlert(activity, ALERT_POSITIVE, message)
    }

    fun showNegativeAlert(activity: Activity, message: String?, action: (() -> Unit?)?=null) {
        showAlert(activity, ALERT_NEGATIVE, message, action = action)
    }

    fun showNeutralAlert(activity: Activity, message: String?) {
        showAlert(activity, ALERT_NEUTRAL, message)
    }

    private fun showAlert(
        activity: Activity,
        alertType: Int,
        message: String?,
        title: String? = "Alert",
        action: (() -> Unit?)? = null
    ) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        message.let { builder.setMessage(it) }
        when (alertType) {
            0 -> {
                builder.setIcon(R.drawable.success_alert)
            }

            1 -> {
                builder.setIcon(R.drawable.ic_error)
            }

            2 -> {
                builder.setIcon(android.R.drawable.ic_dialog_alert)
            }
        }

        builder.setNegativeButton("Ok") { dialogInterface, which ->
            dialogInterface.dismiss()
            action?.invoke()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
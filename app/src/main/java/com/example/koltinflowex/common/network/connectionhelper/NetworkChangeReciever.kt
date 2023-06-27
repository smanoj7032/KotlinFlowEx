package com.example.koltinflowex.common.network.connectionhelper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.koltinflowex.presentation.common.isNetworkAvailable

class NetworkChangeReceiver : BroadcastReceiver {

    private var callback: NetworkChangeCallback? = null

    constructor()
    constructor(callback: NetworkChangeCallback) {
        this.callback = callback
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        try {
            val isConnected = p0?.isNetworkAvailable()
            callback?.onNetworkChanged(isConnected)
        } catch (_: Exception) {
        }
    }
}
package com.example.koltinflowex.presentation.common.base

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val apiCallExecuted: HashMap<String, Boolean> = HashMap()
    fun isApiCallExecuted(apiName: String): Boolean {
        return apiCallExecuted[apiName] ?: false
    }
}
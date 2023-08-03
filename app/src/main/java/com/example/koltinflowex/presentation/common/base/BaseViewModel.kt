package com.example.koltinflowex.presentation.common.base

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    var apiCallExecuted: Boolean = false
}
package com.example.koltinflowex.common.network

class NetworkError(val errorCode: Int, override val message: String?) : Throwable(message)
package com.example.koltinflowex.presentation.common.utils

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.io.Serializable
import java.net.HttpURLConnection



open class SimpleApiResponse : Serializable {
    @SerializedName("code")
    var status = ""

    @SerializedName("message")
    var message: String? = null
        protected set

    @SerializedName("method")
    var method: String? = null
        protected set

    override fun toString(): String {
        return "SimpleApiResponse{success=$status, message='$message'}"
    }

    val isStatusOK: Boolean
        get() = status.toInt() == HttpURLConnection.HTTP_OK
}

open class ApiResponse<T> : SimpleApiResponse() {
    @SerializedName("data")
    val data: T? = null
    override fun toString(): String {
        return "ApiResponse{data=$data}"
    }
}









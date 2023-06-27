package com.example.koltinflowex.presentation.common.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.koltinflowex.common.network.ErrorCodes
import com.example.koltinflowex.common.network.NetworkError
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.common.network.helper.Status
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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









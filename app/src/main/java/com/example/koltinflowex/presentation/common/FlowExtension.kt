package com.example.koltinflowex.presentation.common

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import com.example.koltinflowex.common.network.ErrorCodes
import com.example.koltinflowex.common.network.NetworkError
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.common.network.helper.Status
import com.example.koltinflowex.data.model.Result
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

fun <T> Flow<State<T>>.emitter(
    mutableStateFlow: MutableStateFlow<State<T>>,
    showErrorView: Boolean,
): Job {
    return onEach { state ->
        when (state.status) {
            Status.SUCCESS -> {
                mutableStateFlow.value = state
            }

            Status.ERROR -> {
                mutableStateFlow.value = state
            }

            else -> {
                mutableStateFlow.value = state
            }
        }
    }.catch { throwable ->
        val networkError = parseException(throwable)
        mutableStateFlow.value = State.error(networkError.message, showErrorView)
    }.launchIn(CoroutineScope(Dispatchers.IO))
}


fun <T> MutableStateFlow<State<T>>.customCollector(
    lifecycleOwner: LifecycleOwner,
    onLoading: (Boolean) -> Unit,
    onSuccess: ((data: T) -> Unit)?,
    onError: ((throwable: Throwable, showError: Boolean) -> Unit)?,
) {
    lifecycleOwner.lifecycleScope.launch {
        collect { state ->
            when (state.status) {
                Status.LOADING -> {
                    onLoading.invoke(true)
                }

                Status.SUCCESS -> {
                    onSuccess?.invoke(state.data!!)
                    onLoading.invoke(false)
                }

                Status.ERROR -> {
                    onLoading.invoke(false)
                    onError?.invoke(Throwable(state.message), state.showErrorView)
                }
            }
        }
    }
}

suspend fun <T> executeApiCall(apiCall: suspend () -> Response<T>): Flow<State<T & Any>> {
    return flow {
        try {
            val response = apiCall.invoke()
            Log.e("Response-->>", "executeApiCall: ${Gson().toJson(response.message())}")
            if (response.isSuccessful) {
                emit(State.success(response.body()))
            } else {
                emit(State.error(response.message(), true))
            }
        } catch (e: Exception) {
            emit(State.error(e.message, true))
        }
    }.flowOn(Dispatchers.IO)
}


suspend fun executePagingApiCall(apiCall: suspend () -> PagingData<Result>): Flow<State<PagingData<Result>>> {
    return flow {
        try {
            val result = apiCall.invoke()
            emit(State.success(result))
        } catch (e: Exception) {
            emit(State.error(e.message, true))
        }
    }.flowOn(Dispatchers.IO)
}


fun parseException(throwable: Throwable?): NetworkError {
    return when (throwable) {
        is HttpException -> {
            val exception: HttpException = throwable as HttpException
            NetworkError(exception.code(), exception.message())
        }

        else -> {
            NetworkError(ErrorCodes.OTHER_ERROR, throwable?.message)
        }
    }
}

package com.example.koltinflowex.presentation.common

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.koltinflowex.common.network.ErrorCodes
import com.example.koltinflowex.common.network.NetworkError
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.common.network.helper.Status
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    this.onEach { state ->
        when (state.status) {
            Status.SUCCESS -> mutableStateFlow.value = State.success(state.data)
            Status.ERROR -> mutableStateFlow.value = State.error(state.message, true)
            else -> mutableStateFlow.value = State.loading()
        }
    }.catch { throwable ->
        val networkError = parseException(throwable)
        mutableStateFlow.value = State.error(networkError.message, true)
    }.launchIn(scope)
}


fun <T> StateFlow<State<T>>.customCollector(
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
        emit(State.loading())
        try {
            val response = apiCall.invoke()
            Log.e("Response-->>", "executeApiCall: ${Gson().toJson(response.message())}")
            if (response.isSuccessful) {
                emit(State.success(response.body()))
            } else {
                if (response.message().isEmpty()) {
                    val errorBody = response.errorBody()
                    val errorMessage = errorBody?.string() ?: response.message()
                    emit(State.error(errorMessage, true))
                } else emit(State.error(response.message(), true))
            }
        } catch (e: Exception) {
            emit(State.error(e.message, true))
        }
    }.flowOn(Dispatchers.IO)
}

fun <T : Any> fetchPagingData(
    pagingSourceFactory: () -> PagingSource<Int, T>
): Flow<PagingData<T>> {
    return Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = pagingSourceFactory
    ).flow.flowOn(Dispatchers.IO)
}



fun parseException(throwable: Throwable?): NetworkError {
    return when (throwable) {
        is HttpException -> {
            val exception: HttpException = throwable
            NetworkError(exception.code(), exception.message())
        }
        else -> {
            NetworkError(ErrorCodes.OTHER_ERROR, throwable?.message)
        }
    }
}

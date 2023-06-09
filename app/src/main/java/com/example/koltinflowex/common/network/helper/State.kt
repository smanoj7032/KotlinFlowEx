package com.example.koltinflowex.common.network.helper

class State<out T>(
    val status: Status, val data: T?, val message: String?, val showErrorView: Boolean
) {
    companion object {
        fun <T> success(data: T?): State<T & Any> {
            return State(Status.SUCCESS, data, null, false)
        }

        fun <T> error(msg: String?, showErrorView: Boolean): State<T> {
            return State(Status.ERROR, null, msg, showErrorView)
        }

        fun <T> loading(): State<T> {
            return State(Status.LOADING, null, null, false)
        }
    }
}


enum class Status { SUCCESS, ERROR, LOADING }


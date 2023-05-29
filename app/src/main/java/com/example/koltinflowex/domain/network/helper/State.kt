package com.example.koltinflowex.domain.network.helper

data class State<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): State<T> {
            return State(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String?): State<T> {
            return State(Status.ERROR, null, msg)
        }

        fun <T> loading(): State<T> {
            return State(Status.LOADING, null, null)
        }
    }
}

enum class Status { SUCCESS, ERROR, LOADING }

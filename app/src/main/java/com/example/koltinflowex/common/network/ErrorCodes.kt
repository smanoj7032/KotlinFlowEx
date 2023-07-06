package com.example.koltinflowex.common.network

object ErrorCodes {
    const val SESSION_EXPIRED = 401
    const val OTHER_ERROR = 405
}
data class ErrorResponse(
    val status: String,
    val code: Int,
    val message: String
)

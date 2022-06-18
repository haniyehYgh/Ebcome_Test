package com.example.ebcometest.core.model

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    val messageRes : Int? = -1
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class InternalError<T>(messageRes : Int?):NetworkResult<T>(messageRes = messageRes)
    class NetworkError<T>(message: String?):NetworkResult<T>(message = message)
    class Loading<T>:NetworkResult<T>()
    class Loaded<T>:NetworkResult<T>()
}


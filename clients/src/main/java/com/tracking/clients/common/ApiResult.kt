package com.tracking.clients.common

sealed class ApiResult<T> {
    data class Success<T>(val response: T) : ApiResult<T>()
    data class Error<T>(val message: Message) : ApiResult<T>()

}
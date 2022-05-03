package io.parkmobile.parktunes.global.network

sealed class NetworkStatusHandler<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): NetworkStatusHandler<T>(data)
    class Error<T>(message: String, data: T? = null): NetworkStatusHandler<T>(data, message)
}



package com.example.geminichatapp.data.repo

sealed class ResponseState<T> {
    data object Loading : ResponseState<Unit>()
    data class Success<T>(val data: T) : ResponseState<T>()
    data class Failure(val error: Exception) : ResponseState<Exception>()
}
package com.example.biletzone.models.responses

data class CustomResponse<T>(
    val timestamp: String?,
    val message: String?,
    val payload: T?
)




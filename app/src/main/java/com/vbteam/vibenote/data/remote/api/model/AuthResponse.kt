package com.vbteam.vibenote.data.remote.api.model

data class AuthResponse(
    val accessToken: String,
    val username: String
)

data class ErrorResponse(
    val code: Int,
    val message: String
) 
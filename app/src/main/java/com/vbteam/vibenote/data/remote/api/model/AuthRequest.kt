package com.vbteam.vibenote.data.remote.api.model

data class LoginRequest(
    val login: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val credentials: Credentials
)

data class Credentials(
    val login: String,
    val password: String
) 
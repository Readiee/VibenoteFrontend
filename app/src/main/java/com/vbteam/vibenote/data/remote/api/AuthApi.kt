package com.vbteam.vibenote.data.remote.api

import com.vbteam.vibenote.data.remote.api.model.AuthResponse
import com.vbteam.vibenote.data.remote.api.model.LoginRequest
import com.vbteam.vibenote.data.remote.api.model.RegisterRequest
import retrofit2.http.*

interface AuthApi {
    @POST(ApiConfig.Endpoints.LOGIN)
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST(ApiConfig.Endpoints.REGISTER)
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}

data class ProfileResponse(
    val login: String,
    val username: String
)

data class UpdateProfileRequest(
    val username: String
) 
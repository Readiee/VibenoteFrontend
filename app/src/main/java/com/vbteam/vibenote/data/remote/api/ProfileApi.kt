package com.vbteam.vibenote.data.remote.api

import com.vbteam.vibenote.data.remote.api.model.AuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ProfileApi {
    @GET(ApiConfig.Endpoints.PROFILE)
    suspend fun getProfile(): ProfileResponse

    @PUT(ApiConfig.Endpoints.PROFILE)
    suspend fun updateProfile(@Body request: UpdateProfileRequest): AuthResponse

    @POST(ApiConfig.Endpoints.LOGOUT)
    suspend fun logout()
}

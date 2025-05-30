package com.vbteam.vibenote.data.remote.api

import android.util.Log
import com.vbteam.vibenote.data.auth.AuthManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authManager: AuthManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { authManager.authToken.first() }
        Log.d("AuthInterceptor", "Intercepting request to: ${chain.request().url}")
        Log.d("AuthInterceptor", "Current token: $token")

        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder().apply {
            token?.let {
                Log.d("AuthInterceptor", "Adding Bearer token to request")
                addHeader("Authorization", "Bearer $it")
                Log.d("AuthInterceptor", "Request headers:")
                build().headers.forEach { header ->
                    Log.d("AuthInterceptor", "${header.first}: ${header.second}")
                }
            } ?: Log.d("AuthInterceptor", "No token available, skipping auth header")
        }.build()

        return chain.proceed(modifiedRequest)
    }
}   
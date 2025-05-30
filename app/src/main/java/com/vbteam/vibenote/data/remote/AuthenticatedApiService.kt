package com.vbteam.vibenote.data.remote

import android.util.Log
import com.vbteam.vibenote.data.remote.api.ProfileApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatedApiService @Inject constructor(
    private val profileApi: ProfileApi
) {
    suspend fun logout() {
        try {
            profileApi.logout()
        } catch (e: Exception) {
            Log.e(TAG, "Logout failed", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "AuthenticatedApiService"
    }
} 
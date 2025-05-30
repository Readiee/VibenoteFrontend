package com.vbteam.vibenote.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.vbteam.vibenote.data.model.User
import com.vbteam.vibenote.data.remote.api.AuthApi
import com.vbteam.vibenote.data.remote.api.model.Credentials
import com.vbteam.vibenote.data.remote.api.model.LoginRequest
import com.vbteam.vibenote.data.remote.api.model.RegisterRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authApi: AuthApi
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken.asStateFlow()

    init {
        loadSavedData()
    }

    private fun loadSavedData() {
        val token = prefs.getString(KEY_AUTH_TOKEN, null)
        val username = prefs.getString(KEY_USERNAME, null)
        val login = prefs.getString(KEY_LOGIN, null)

        _authToken.value = token
        if (username != null && login != null) {
            _currentUser.value = User(username = username, login = login)
        }
    }

    private fun saveAuthData(token: String, username: String, login: String) {
        prefs.edit().apply {
            putString(KEY_AUTH_TOKEN, token)
            putString(KEY_USERNAME, username)
            putString(KEY_LOGIN, login)
            apply()
        }
        _authToken.value = token
        _currentUser.value = User(username = username, login = login)
    }

    suspend fun login(login: String, password: String): Result<User> {
        return try {
            val response = authApi.login(LoginRequest(login, password))
            saveAuthData(response.accessToken, response.username, login)
            Result.success(User(username = response.username, login = login))
        } catch (e: Exception) {
            Log.e(TAG, "Login failed", e)
            Result.failure(e)
        }
    }

    suspend fun register(username: String, login: String, password: String): Result<User> {
        return try {
            val response = authApi.register(RegisterRequest(
                username = username,
                credentials = Credentials(login = login, password = password)
            ))
            saveAuthData(response.accessToken, response.username, login)
            Result.success(User(username = response.username, login = login))
        } catch (e: Exception) {
            Log.e(TAG, "Registration failed", e)
            Result.failure(e)
        }
    }

    fun clearAuthData() {
        prefs.edit().clear().apply()
        _authToken.value = null
        _currentUser.value = null
    }

    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USERNAME = "username"
        private const val KEY_LOGIN = "login"
        private const val TAG = "AuthManager"
    }
} 
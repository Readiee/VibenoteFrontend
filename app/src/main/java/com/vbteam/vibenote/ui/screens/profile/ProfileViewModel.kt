package com.vbteam.vibenote.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbteam.vibenote.data.auth.AuthManager
import com.vbteam.vibenote.data.remote.api.ProfileApi
import com.vbteam.vibenote.data.remote.api.UpdateProfileRequest
import com.vbteam.vibenote.data.remote.AuthenticatedApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val profileApi: ProfileApi,
    private val authenticatedApiService: AuthenticatedApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    var name by mutableStateOf("")
        private set

    var nameError by mutableStateOf<String?>(null)
        private set

    var showLogoutDialog by mutableStateOf(false)
        private set

    private var currentUsername: String = ""

    val isButtonEnabled: Boolean
        get() = name.isNotBlank() && name != currentUsername

    init {
        viewModelScope.launch {
            authManager.currentUser
                .collect { user ->
                    _uiState.update {
                        it.copy(
                            isAuthenticated = user != null,
                            currentUser = user
                        )
                    }
                    if (user != null) {
                        fetchProfileInfo()
                    }
                }
        }
    }

    private suspend fun fetchProfileInfo() {
        try {
            _uiState.update { it.copy(isLoading = true) }
            val profileInfo = profileApi.getProfile()
            currentUsername = profileInfo.username // Сохраняем текущее имя
            name = profileInfo.username
            _uiState.update {
                it.copy(
                    isLoading = false,
                    currentUser = it.currentUser?.copy(
                        username = profileInfo.username,
                        login = profileInfo.login
                    )
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun onNameChanged(newName: String) {
        name = newName
        nameError = null
    }

    fun changeUsername() {
        if (name.isBlank()) {
            nameError = "Имя не может быть пустым"
            return
        }

        if (name == currentUsername) {
            nameError = "Новое имя совпадает с текущим"
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val response = profileApi.updateProfile(UpdateProfileRequest(username = name))
                currentUsername = name // Обновляем текущее имя после успешного изменения
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUser = it.currentUser?.copy(username = name),
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Произошла ошибка при обновлении имени"
                    )
                }
            }
        }
    }

    // Функции для управления диалогом
    fun showLogoutConfirmation() {
        showLogoutDialog = true
    }

    fun dismissLogoutDialog() {
        showLogoutDialog = false
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                authenticatedApiService.logout()
                authManager.clearAuthData()
                showLogoutDialog = false
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Произошла ошибка при выходе"
                    )
                }
            }
        }
    }
}
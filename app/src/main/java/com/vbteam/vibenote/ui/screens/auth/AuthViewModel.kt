package com.vbteam.vibenote.ui.screens.auth

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vbteam.vibenote.data.auth.AuthManager
import com.vbteam.vibenote.data.remote.AuthenticatedApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val authenticatedApiService: AuthenticatedApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var nameError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var confirmPasswordError by mutableStateOf<String?>(null)

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
                }
        }
    }

    fun onEmailChanged(newEmail: String) {
        email = newEmail
        if (emailError != null) emailError = null
    }
    fun onPasswordChanged(newPassword: String) {
        password = newPassword
        if (passwordError != null) passwordError = null
    }
    fun onNameChanged(newName: String) {
        name = newName
        if (nameError != null) nameError = null
    }
    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        if (confirmPasswordError != null) confirmPasswordError = null
    }

    private fun validateCommon(): Boolean {
        var valid = true

        if (email.isBlank()) {
            emailError = "Введите email"
            valid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Некорректный email"
            valid = false
        }
        if (password.isBlank()) {
            passwordError = "Введите пароль"
            valid = false
        }

        return valid
    }

    fun validateSignIn(): Boolean {
        return validateCommon()
    }

    fun validateSignUp(): Boolean {
        var valid = validateCommon()

        nameError = if (name.isBlank()) {
            valid = false
            "Введите имя"
        } else null

        if (password.length < 6) {
            passwordError = "Минимум 6 символов"
            valid = false
        }

        confirmPasswordError = if (confirmPassword != password) {
            valid = false
            "Пароли не совпадают"
        } else null

        return valid
    }

    fun onSignInClick(): Boolean {
        if (!validateSignIn()) return false
        
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                login(email, password)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sign in failed", e)
                _uiState.update { it.copy(error = "Не удалось войти. Проверьте учетные данные.") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
        return true
    }

    fun onRegisterClick(): Boolean {
        if (!validateSignUp()) return false
        
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                register(name, email, password)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration failed", e)
                _uiState.update { it.copy(error = "Не удалось зарегистрироваться. Попробуйте другой email.") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
        return true
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val result = authManager.login(login, password)
                result.fold(
                    onSuccess = { user ->
                        _uiState.update { 
                            it.copy(
                                isAuthenticated = true,
                                currentUser = user,
                                error = null
                            )
                        }
                    },
                    onFailure = { e ->
                        Log.e("AuthViewModel", "Login failed", e)
                        _uiState.update { 
                            it.copy(error = "Не удалось войти. Проверьте учетные данные.")
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login failed", e)
                _uiState.update { 
                    it.copy(error = "Произошла непредвиденная ошибка. Повторите попытку позже.")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun register(username: String, login: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val result = authManager.register(username, login, password)

                result.fold(
                    onSuccess = { user ->
                        _uiState.update { 
                            it.copy(
                                isAuthenticated = true,
                                currentUser = user,
                                error = null
                            )
                        }
                    },
                    onFailure = { e ->
                        Log.e("AuthViewModel", "Registration failed", e)
                        _uiState.update { it.copy(error = "Не удалось зарегистрироваться. Попробуйте другой email.") }
                    }
                )
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration failed", e)
                _uiState.update { it.copy(error = "Произошла непредвиденная ошибка. Повторите попытку позже.") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                authenticatedApiService.logout()
                authManager.clearAuthData()
                _uiState.update { 
                    it.copy(
                        isAuthenticated = false,
                        currentUser = null,
                        error = null
                    )
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Logout failed", e)
                _uiState.update { it.copy(error = "Не удалось выйти из аккаунта. Попробуйте еще раз.") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

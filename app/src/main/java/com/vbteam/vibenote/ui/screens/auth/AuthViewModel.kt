package com.vbteam.vibenote.ui.screens.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var nameError by mutableStateOf<String?>(null)
    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var confirmPasswordError by mutableStateOf<String?>(null)

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
        return validateSignIn()
    }

    fun onRegisterClick(): Boolean {
        return validateSignUp()
    }
}

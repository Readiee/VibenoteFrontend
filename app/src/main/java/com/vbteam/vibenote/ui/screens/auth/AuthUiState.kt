package com.vbteam.vibenote.ui.screens.auth

import com.vbteam.vibenote.data.model.User

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val error: String? = null
) 
package com.vbteam.vibenote.ui.screens.profile

import com.vbteam.vibenote.data.model.User

data class ProfileUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val error: String? = null
)
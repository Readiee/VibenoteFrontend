package com.vbteam.vibenote.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor() {
    var isLoggedIn: Boolean = false
    var userId: String? = null
    var authToken: String? = null

    fun login(token: String, id: String) {
        isLoggedIn = true
        userId = id
        authToken = token
    }

    fun logout() {
        isLoggedIn = false
        userId = null
        authToken = null
    }
}
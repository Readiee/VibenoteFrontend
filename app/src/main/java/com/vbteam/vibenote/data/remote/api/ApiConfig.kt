package com.vbteam.vibenote.data.remote.api

object ApiConfig {
    const val BASE_URL = "http://10.0.2.2:8511/"

    object Timeouts {
        const val CONNECT_TIMEOUT_SECONDS = 30L
        const val READ_TIMEOUT_SECONDS = 30L
        const val WRITE_TIMEOUT_SECONDS = 30L
    }

    object Headers {
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_TYPE_VALUE = "application/json"
        const val AUTH_HEADER = "Authorization"
    }

    object Endpoints {
        // Auth endpoints
        const val LOGIN = "users/login"
        const val REGISTER = "users/register"
        const val LOGOUT = "users/logout"
        const val PROFILE = "users/"

        // Entry endpoints
        const val ENTRIES = "entries"
        const val ENTRY = "entries/{id}"
        const val ANALYSIS = "analysis/{entryId}"
    }
} 
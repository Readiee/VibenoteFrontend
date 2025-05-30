package com.vbteam.vibenote.data.remote.api

sealed class ApiException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

class UnauthorizedException(message: String? = "Invalid token") : ApiException(message)
class ForbiddenException(message: String? = "Not enough access rights") : ApiException(message)
class AnalysisException(message: String? = "Analysis error") : ApiException(message)
class NetworkException(message: String? = "Network error", cause: Throwable? = null) : ApiException(message, cause) 
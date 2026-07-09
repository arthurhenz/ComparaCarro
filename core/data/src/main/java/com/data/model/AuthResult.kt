package com.data.model

/** Outcome of an authentication attempt. [Error.message] is already localized (pt-BR) for display. */
sealed class AuthResult {
    data class Success(val user: AuthUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

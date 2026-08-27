package com.data.model

/** Outcome of an authentication attempt. [Error.message] is already localized (pt-BR) for display. */
sealed class AuthResult {
    data class Success(val user: AuthUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

/** Outcome of a password-reset request. */
sealed class PasswordResetResult {
    /** Firebase accepted the request and (if the account exists) the e-mail is on its way. */
    data object Sent : PasswordResetResult()

    /** The account only signs in with Google — there is no password to reset. */
    data object GoogleOnly : PasswordResetResult()

    data class Error(val message: String) : PasswordResetResult()
}

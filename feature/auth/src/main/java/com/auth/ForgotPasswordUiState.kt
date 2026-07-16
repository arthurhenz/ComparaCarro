package com.auth

/**
 * Form state for the forgot-password screen. [emailSent] flips the screen into its success state;
 * [googleOnlyAccount] flips it into the "this account signs in with Google" notice instead.
 */
data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val emailSent: Boolean = false,
    val googleOnlyAccount: Boolean = false,
)

package com.auth

/** Shared form state for the login and signup screens. */
data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

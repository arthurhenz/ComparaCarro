package com.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.data.model.AuthResult
import com.data.repository.AuthRepository
import com.navigation.routes.ForgotPasswordRoute
import com.navigation.routes.HomeScreenRoute
import com.navigation.routes.ProfileRoute
import com.navigation.routes.SignupRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val authRepository: AuthRepository,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Preencha e-mail e senha.")
            return
        }
        submit { authRepository.signIn(email, password) }
    }

    fun loginWithGoogle(idToken: String) = submit { authRepository.signInWithGoogle(idToken) }

    /** Called by the screen while the Google account picker is open. */
    fun onGoogleStarted() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
    }

    /** Called by the screen when Credential Manager fails or is cancelled. */
    fun onGoogleFailed(message: String) {
        _state.value = _state.value.copy(isLoading = false, errorMessage = message)
    }

    fun goToSignup() = navigate(SignupRoute, NavOptions(singleTop = true))

    fun goToForgotPassword() = navigate(ForgotPasswordRoute, NavOptions(singleTop = true))

    fun continueWithoutLogin() = navigate(HomeScreenRoute, NavOptions(singleTop = true))

    private fun submit(request: suspend () -> AuthResult) {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val result = request()) {
                is AuthResult.Success -> {
                    _state.value = _state.value.copy(isLoading = false)
                    onAuthenticated()
                }
                is AuthResult.Error ->
                    _state.value = _state.value.copy(isLoading = false, errorMessage = result.message)
            }
        }
    }

    // Optional-login flow: land the user back on Profile (the only entry point), now signed in.
    private fun onAuthenticated() =
        navigate(ProfileRoute, NavOptions(popUpTo = ProfileRoute, singleTop = true))
}

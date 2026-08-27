package com.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.data.model.AuthResult
import com.data.repository.AuthRepository
import com.navigation.routes.ProfileRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignupViewModel(
    private val authRepository: AuthRepository,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun signup(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Preencha todos os campos.")
            return
        }
        submit { authRepository.signUp(name, email, password) }
    }

    fun signupWithGoogle(idToken: String) = submit { authRepository.signInWithGoogle(idToken) }

    fun onGoogleStarted() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
    }

    fun onGoogleFailed(message: String) {
        _state.value = _state.value.copy(isLoading = false, errorMessage = message)
    }

    fun goToLogin() = goBack()

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

    private fun onAuthenticated() =
        navigate(ProfileRoute, NavOptions(popUpTo = ProfileRoute, singleTop = true))
}

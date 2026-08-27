package com.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.Navigator
import com.data.model.PasswordResetResult
import com.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ForgotPasswordViewModel(
    private val authRepository: AuthRepository,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state: StateFlow<ForgotPasswordUiState> = _state.asStateFlow()

    fun sendResetLink(email: String) {
        if (email.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Informe seu e-mail.")
            return
        }
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val result = authRepository.sendPasswordReset(email)) {
                is PasswordResetResult.Sent ->
                    _state.value = ForgotPasswordUiState(emailSent = true)
                is PasswordResetResult.GoogleOnly ->
                    _state.value = ForgotPasswordUiState(googleOnlyAccount = true)
                is PasswordResetResult.Error ->
                    _state.value = ForgotPasswordUiState(errorMessage = result.message)
            }
        }
    }

    fun backToLogin() = goBack()
}

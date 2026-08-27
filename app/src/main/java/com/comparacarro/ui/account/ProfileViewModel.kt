package com.comparacarro.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.Navigator
import com.common.utils.stateInWhileSubscribed
import com.data.model.AuthUser
import com.data.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    private val authRepository: AuthRepository,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    /** The signed-in user, or `null` when signed out (the route then shows the login screen). */
    val user: StateFlow<AuthUser?> =
        authRepository.currentUser.stateInWhileSubscribed(viewModelScope, authRepository.currentUserOrNull())

    fun logout() = authRepository.signOut()
}

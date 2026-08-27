package com.comparacarro.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.common.utils.stateInWhileSubscribed
import com.data.model.AuthUser
import com.data.repository.AuthRepository
import com.navigation.routes.LoginRoute
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    private val authRepository: AuthRepository,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    /** The signed-in user, or `null` while browsing as a guest (optional login). */
    val user: StateFlow<AuthUser?> =
        authRepository.currentUser.stateInWhileSubscribed(viewModelScope, authRepository.currentUserOrNull())

    fun signIn() = navigate(LoginRoute, NavOptions(singleTop = true))

    fun logout() = authRepository.signOut()
}

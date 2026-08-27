package com.comparacarro.ui.account

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.auth.LoginScreenHost
import com.favorites.navigateToBottomTab
import com.navigation.EntryProvider
import com.navigation.routes.ProfileRoute
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.profileScreenRoute() {
    entry<ProfileRoute> {
        val viewModel: ProfileViewModel = koinViewModel()
        val user by viewModel.user.collectAsStateWithLifecycle()

        val currentUser = user
        if (currentUser == null) {
            // No session: the Profile tab shows the login screen itself.
            LoginScreenHost()
        } else {
            ProfileScreen(
                name = currentUser.name?.takeIf { it.isNotBlank() } ?: "Usuário",
                email = currentUser.email.orEmpty(),
                photoUrl = currentUser.photoUrl,
                onLogout = viewModel::logout,
                onNavigate = { tab -> viewModel.navigateToBottomTab(tab) },
            )
        }
    }
}

class ProfileScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { profileScreenRoute() }
}

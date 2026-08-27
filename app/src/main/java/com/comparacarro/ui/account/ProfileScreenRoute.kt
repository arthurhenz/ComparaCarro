package com.comparacarro.ui.account

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.favorites.navigateToBottomTab
import com.navigation.EntryProvider
import com.navigation.routes.ProfileRoute
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.profileScreenRoute() {
    entry<ProfileRoute> {
        val viewModel: ProfileViewModel = koinViewModel()
        val user by viewModel.user.collectAsStateWithLifecycle()

        ProfileScreen(
            isLoggedIn = user != null,
            accountName = user?.name?.takeIf { it.isNotBlank() } ?: "Visitante",
            accountEmail = user?.email ?: "Entre para salvar seus favoritos",
            onLogout = viewModel::logout,
            onSignIn = viewModel::signIn,
            onNavigate = { tab -> viewModel.navigateToBottomTab(tab) },
        )
    }
}

class ProfileScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { profileScreenRoute() }
}

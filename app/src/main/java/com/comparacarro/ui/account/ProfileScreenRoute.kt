package com.comparacarro.ui.account

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.favorites.navigateToBottomTab
import com.navigation.EntryProvider
import com.navigation.routes.ProfileRoute
import org.koin.compose.koinInject

fun EntryProviderScope<NavKey>.profileScreenRoute() {
    entry<ProfileRoute> {
        val navigator = koinInject<Navigator>()
        ProfileScreen(
            onNavigate = { tab -> navigator.navigateToBottomTab(tab) },
        )
    }
}

class ProfileScreenProvider : EntryProvider {
    override fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit = { profileScreenRoute() }
}

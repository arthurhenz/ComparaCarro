package com.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

fun interface EntryProvider {
    fun entryProvider(): EntryProviderScope<NavKey>.() -> Unit
}

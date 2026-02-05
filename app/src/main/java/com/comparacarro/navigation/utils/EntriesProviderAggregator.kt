package com.comparacarro.navigation.utils

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.core.annotation.Single

@Single
class EntriesProviderAggregator(val entries : List<EntryProviderScope<NavKey>.() -> Unit>)

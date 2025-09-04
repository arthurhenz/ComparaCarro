package com.home

sealed class HomeScreenEvent {
    data object ReloadCards : HomeScreenEvent()
}

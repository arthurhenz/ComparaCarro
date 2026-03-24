package com.common.navigation

interface Navigator {
    fun navigate(route: Any, options: NavOptions = NavOptions())
    fun goBack()
}

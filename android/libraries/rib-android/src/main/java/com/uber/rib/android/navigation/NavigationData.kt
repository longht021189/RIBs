package com.uber.rib.android.navigation

class NavigationData(
    val tag: String,
    val name: String,
    private val manager: NavigationManager
) {
    val navigation: Navigation by lazy {
        Navigation(manager, this)
    }
}
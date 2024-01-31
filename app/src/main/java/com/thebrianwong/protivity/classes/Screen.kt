package com.thebrianwong.protivity.classes

sealed class Screen(val name: String, val route: String) {
    object HomeScreen : Screen("Home", "home")
    object SettingsScreen : Screen("Settings", "settings")
}

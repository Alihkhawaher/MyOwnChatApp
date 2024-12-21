package com.example.myownchat.navigation

sealed class Screen(val route: String) {
    object ApiKey : Screen("api_key_screen")
    object Chat : Screen("chat_screen")
}

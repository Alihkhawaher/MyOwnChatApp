package com.example.myownchat.navigation

import androidx.annotation.DrawableRes
import com.example.myownchat.R


sealed class Screen(val title: String, val route: String) {
    sealed class BottomScreen(
        val bTitle: String,
        val bRoute: String,
        @DrawableRes val icon: Int
    ): Screen(bTitle, bRoute){
        object Chats: BottomScreen("Chats", "chatsscreen", R.drawable.baseline_chat_24)
        object Settings: BottomScreen("Settings", "settingsscreen", R.drawable.baseline_settings_24)
        object Contacts: BottomScreen("Contacts", "contactsscreen", R.drawable.baseline_people_24)
    }
}

val screensInBottomBar = listOf(
    Screen.BottomScreen.Contacts,
    Screen.BottomScreen.Chats,
    Screen.BottomScreen.Settings
)
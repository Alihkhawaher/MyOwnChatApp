package com.example.myownchat.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myownchat.navigation.Screen

class MainAppViewModel: ViewModel() {
    private val _currentScreen : MutableState<Screen> = mutableStateOf(Screen.BottomScreen.Chats)

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen){
        _currentScreen.value = screen
        Log.d("setCurrentScreen", screen.title)
    }
}
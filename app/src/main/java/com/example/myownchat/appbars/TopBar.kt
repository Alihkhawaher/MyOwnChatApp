package com.example.myownchat.appbars

import android.util.Log
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myownchat.R
import com.example.myownchat.navigation.Screen
import com.example.myownchat.viewmodel.MainAppViewModel

@Composable
fun AppTopBar(
    viewModel: MainAppViewModel,
){
    val currentScreen = remember{ viewModel.currentScreen.value }
    Log.d("AppTopBar", currentScreen.title)

    //TODO DEBUG CURRENT SCREEN
    TopAppBar(
        modifier = Modifier.wrapContentSize(),
        title = {
            Text(text = currentScreen.title, fontSize = 22.sp)
        },
        actions = {
            if (currentScreen == Screen.BottomScreen.Chats){
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.Message, contentDescription = "New chat")
                }
            }else if(currentScreen == Screen.BottomScreen.Contacts){
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.PersonAdd, contentDescription = "Add contact")
                }
            }
            else{
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "Exit")
                }
            }
        }
    )
}
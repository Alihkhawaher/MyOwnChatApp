package com.example.myownchat.appbars

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myownchat.navigation.Screen
import com.example.myownchat.navigation.screensInBottomBar
import com.example.myownchat.viewmodel.MainAppViewModel


@Composable
fun AppBottomBar(
    navHostController: NavHostController,
    viewModel: MainAppViewModel
){

    val currentScreen = remember{ viewModel.currentScreen.value }
    val title = remember{ mutableStateOf(currentScreen.title) }

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (
        currentScreen == Screen.BottomScreen.Chats ||
        currentScreen == Screen.BottomScreen.Contacts ||
        currentScreen == Screen.BottomScreen.Settings
        ){
        BottomAppBar(
            modifier = Modifier.wrapContentSize()
        ) {
            screensInBottomBar.forEach{ item ->
                val isSelected = currentRoute == item.bRoute
                val tint = if (isSelected) Color.White else Color.Black
                BottomNavigationItem(
                    selected = isSelected,
                    onClick = {
                        navHostController.navigate(item.bRoute){
                            viewModel.setCurrentScreen(item)
                            popUpTo(0)
                        }
                        title.value = item.title
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            tint = tint
                        )
                    },
                    label = {
                        Text(text = item.title, color = tint)
                    },
                    selectedContentColor = Color.White
                )
            }
        }
    }
}

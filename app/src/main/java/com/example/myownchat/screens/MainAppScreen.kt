package com.example.myownchat.screens

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myownchat.navigation.Screen
import com.example.myownchat.navigation.screensInBottomBar
import com.example.myownchat.viewmodel.MainAppViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainAppScreen(
    navHostController: NavHostController
){

    /*Scaffold state and coroutine scope is used for:
    * - Coroutine scope:
    *   is for opening, closing, doing operations in different streams to make application work
    *   without freezing
    * - Scaffold state:
    *   is necessary for scaffold to work
    */
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()


    /*viewModel and currentScreen is used for:
    * - viewModel:
    *   to get and know current screen
    * - currentScreen:
    *   is assigned value of _currentScreen
    * - title:
    *   to know the title of the page
    */
    val viewModel: MainAppViewModel = viewModel()
    val currentScreen = remember{ viewModel.currentScreen.value }
    val title = remember{ mutableStateOf(currentScreen.title) }

    /*
    * It helps us to discover on what page we are. We take value of our current route and assign it
    * to the currentRoute variable
    */
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    /*
    * appTopBar - top bar of our application, it is different, depending on the current route
    * appBottomBar - bottom ar of our application
    */
    val appTopBar: @Composable () -> Unit = {}
    val appBottomBar: @Composable () -> Unit = {
        if (currentScreen == Screen.BottomScreen.Chats){
            BottomAppBar(
                modifier = Modifier.wrapContentSize()
            ) {
                screensInBottomBar.forEach{item ->
                    val isSelected = currentRoute == item.bRoute
                    val tint = if (isSelected) Color.White else Color.Black
                    BottomNavigationItem(
                        selected = isSelected,
                        onClick = {
                            navHostController.navigate(item.bRoute){
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

    Scaffold(
        topBar = appTopBar,
        bottomBar = appBottomBar,
        scaffoldState = scaffoldState
    ) {
        val pd = it
        Text(text = "hello")
    }

}
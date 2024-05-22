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
import androidx.navigation.compose.rememberNavController
import com.example.myownchat.appbars.AppBottomBar
import com.example.myownchat.appbars.AppBottomBar
import com.example.myownchat.appbars.AppTopBar
import com.example.myownchat.navigation.NavigationGraph
import com.example.myownchat.navigation.Screen
import com.example.myownchat.navigation.screensInBottomBar
import com.example.myownchat.viewmodel.AuthViewModel
import com.example.myownchat.viewmodel.MainAppViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainAppView(){

    val navHostController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

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


    Scaffold(
        topBar = {
            AppTopBar(
                viewModel = viewModel
            )
        },
        bottomBar = {
            AppBottomBar(
                navHostController = navHostController,
                viewModel = viewModel
            )
        },
        scaffoldState = scaffoldState
    ) {
        val pd = it
        NavigationGraph(
            navHostController = navHostController,
            authViewModel = authViewModel,
            userIsAuthentificated = firebaseAuth.currentUser != null,
        )
    }

}
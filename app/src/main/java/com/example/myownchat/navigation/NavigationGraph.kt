package com.example.myownchat.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myownchat.screens.ChatsScreen
import com.example.myownchat.screens.ContactsScreen
import com.example.myownchat.screens.MainAppView
import com.example.myownchat.screens.LoginScreen
import com.example.myownchat.screens.SettingsScreen
import com.example.myownchat.screens.SignUpScreen
import com.example.myownchat.viewmodel.AuthViewModel
import com.example.myownchat.viewmodel.MainAppViewModel

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    userIsAuthentificated: Boolean,
){

    NavHost(
        navController = navHostController,
        startDestination = if (userIsAuthentificated){
            Route.chatsRoute.routeToScreen
        } else { Route.loginRoute.routeToScreen }
    ) {
        composable(Route.loginRoute.routeToScreen){
            LoginScreen(
                onNavigationToSignUp = {
                    navHostController.navigate(Route.signUpRoute.routeToScreen){
                        popUpTo(0)
                    }
                },
                onNavigationToChats = {
                    navHostController.navigate(Route.chatsRoute.routeToScreen){
                        popUpTo(0)
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable(Route.signUpRoute.routeToScreen){
            SignUpScreen(
                onNavigationToLogin = {
                    navHostController.navigate(Route.loginRoute.routeToScreen){
                        popUpTo(0)
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable(Route.chatsRoute.routeToScreen){
            ChatsScreen()
        }

        composable(Route.contactsRoute.routeToScreen){
            ContactsScreen()
        }

        composable(Route.settingsRoute.routeToScreen){
            SettingsScreen()
        }
    }

}
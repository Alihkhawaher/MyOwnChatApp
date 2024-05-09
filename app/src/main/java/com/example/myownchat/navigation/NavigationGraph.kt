package com.example.myownchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myownchat.screens.AllChatsScreen
import com.example.myownchat.screens.LoginScreen
import com.example.myownchat.screens.SignUpScreen
import com.example.myownchat.viewmodel.AuthViewModel

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    authViewModel: AuthViewModel
){

    NavHost(navController = navHostController, startDestination = Route.loginRoute.routeToScreen) {
        composable(Route.loginRoute.routeToScreen){
            LoginScreen(
                onNavigationToSignUp = {
                    navHostController.navigate(Route.signUpRoute.routeToScreen){
                        popUpTo(0)
                    }
                },
                onNavigationToAllChats = {
                    navHostController.navigate(Route.allChatsRoute.routeToScreen){
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

        composable(Route.allChatsRoute.routeToScreen){
            AllChatsScreen()
        }
    }

}
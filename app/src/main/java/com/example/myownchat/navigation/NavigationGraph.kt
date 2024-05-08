package com.example.myownchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myownchat.screens.LoginScreen
import com.example.myownchat.screens.SignUpScreen

@Composable
fun NavigationGraph(
    navHostController: NavHostController
){

    NavHost(navController = navHostController, startDestination = Route.loginRoute.routeToScreen) {
        composable(Route.loginRoute.routeToScreen){
            LoginScreen(
                onNavigationToSignUp = {
                    navHostController.navigate(Route.signUpRoute.routeToScreen)
                }
            )
        }

        composable(Route.signUpRoute.routeToScreen){
            SignUpScreen(
                onNavigationToLogin = {
                    navHostController.navigate(Route.loginRoute.routeToScreen)
                }
            )
        }
    }

}
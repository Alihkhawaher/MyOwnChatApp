package com.example.myownchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myownchat.screens.*
import com.example.myownchat.viewmodel.MainAppViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: MainAppViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ApiKey.route
    ) {
        composable(route = Screen.ApiKey.route) {
            ApiKeyScreen(
                viewModel = viewModel,
                onApiKeySet = {
                    navController.navigate(Screen.Chat.route) {
                        popUpTo(Screen.ApiKey.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Chat.route) {
            ChatsScreen(viewModel = viewModel)
        }
    }
}

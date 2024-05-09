package com.example.myownchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myownchat.navigation.NavigationGraph
import com.example.myownchat.screens.LoginScreen
import com.example.myownchat.ui.theme.MyOwnChatTheme
import com.example.myownchat.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope


class MainActivity : ComponentActivity(){

    val topBar:@Composable () -> Unit = {

    }

    val bottomBar:@Composable () -> Unit = {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyOwnChatTheme {

                val navHostController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(
                        navHostController = navHostController,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

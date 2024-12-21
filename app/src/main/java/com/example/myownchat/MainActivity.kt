package com.example.myownchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myownchat.screens.ApiKeyScreen
import com.example.myownchat.screens.ChatsScreen
import com.example.myownchat.ui.theme.MyOwnChatTheme
import com.example.myownchat.viewmodel.MainAppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainAppViewModel = viewModel()
            val isApiKeySet by viewModel::isApiKeySet

            MyOwnChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!isApiKeySet) {
                        ApiKeyScreen(
                            viewModel = viewModel,
                            onApiKeySet = { /* Key is set in viewModel */ }
                        )
                    } else {
                        ChatsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

package com.example.myownchat.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myownchat.R
import com.example.myownchat.providers.LocalLLMProvider
import com.example.myownchat.viewmodel.MainAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiKeyScreen(
    viewModel: MainAppViewModel,
    onApiKeySet: () -> Unit
) {
    var apiKey by remember { mutableStateOf("") }
    var serverUrl by remember { mutableStateOf("http://localhost:8080") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Provider Selection
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.selectedProvider?.getName() ?: "Select Provider",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                viewModel.availableProviders.forEach { provider ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(provider.getName())
                                Text(
                                    provider.getDescription(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        },
                        onClick = {
                            viewModel.selectProvider(provider)
                            expanded = false
                            apiKey = ""
                        }
                    )
                }
            }
        }

        // Server URL input for LocalLLM
        if (viewModel.selectedProvider is LocalLLMProvider) {
            OutlinedTextField(
                value = serverUrl,
                onValueChange = {
                    serverUrl = it
                    viewModel.updateLocalLLMUrl(it)
                },
                label = { Text("Server URL") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )
        }

        // API Key input for providers that require it
        if (viewModel.selectedProvider?.requiresApiKey() == true) {
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("Enter ${viewModel.selectedProvider?.getName()} API Key") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )
        }

        Button(
            onClick = {
                if (viewModel.selectedProvider != null &&
                    (!viewModel.selectedProvider!!.requiresApiKey() || apiKey.isNotBlank())
                ) {
                    if (viewModel.selectedProvider!!.requiresApiKey()) {
                        viewModel.setApiKey(apiKey)
                    }
                    onApiKeySet()
                }
            },
            enabled = viewModel.selectedProvider != null &&
                    (!viewModel.selectedProvider!!.requiresApiKey() || apiKey.isNotBlank()),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Chatting")
        }
    }
}

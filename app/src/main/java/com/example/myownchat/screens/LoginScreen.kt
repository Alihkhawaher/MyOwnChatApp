package com.example.myownchat.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onNavigationToSignUp: () -> Unit
){

    val login = remember{ mutableStateOf("") }
    val password = remember{ mutableStateOf("") }
    var showPassword by remember{ mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                value = login.value,
                onValueChange = {login.value = it},
                label = { Text(text = "Login") },
                placeholder = { Text(text = "Type your login") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Login, contentDescription = null)
                }
            )
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                value = password.value,
                onValueChange = {password.value = it},
                visualTransformation = if (!showPassword) PasswordVisualTransformation()
                                        else VisualTransformation.None,
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Enter your password") },
                leadingIcon = {
                    IconButton(onClick = {
                        showPassword = (!showPassword)
                    }) {
                        val imageVector = if(showPassword){Icons.Filled.Visibility}else{Icons.Filled.VisibilityOff}
                        Icon(imageVector = imageVector, contentDescription = null)
                    }
                }
            )
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))

        Button(
            onClick = { /*TODO*/ },

        ) {
            Text(text = "LOG IN")
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp))

        Text(
            text = "No account, then sign up!",
            modifier = Modifier.clickable(true) {
                onNavigationToSignUp()
            }
        )
    }
}
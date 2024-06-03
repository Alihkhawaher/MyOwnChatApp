package com.example.myownchat.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.ui.res.painterResource
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myownchat.FIREBASE_AUTH
import com.example.myownchat.data.Result
import com.example.myownchat.data.User
import com.example.myownchat.navigation.NavigationGraph
import com.example.myownchat.navigation.Route
import com.example.myownchat.navigation.Screen
import com.example.myownchat.navigation.screensInBottomBar
import com.example.myownchat.viewmodel.AuthViewModel
import com.example.myownchat.viewmodel.MainAppViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlin.math.log

@Composable
fun MainAppView(){

    val navHostController = rememberNavController()
    val context = LocalContext.current

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


    val authViewModel: AuthViewModel = viewModel()
    /*
    * Setting up the current user as a global variable to use it across the application
    */
    authViewModel.getUser()
    val currentUser: User? = authViewModel.userFromFirebasePairUser.value
    Log.d("start user", "user = ${currentUser.toString()}")

    /*
    * It helps us to discover on what page we are. We take value of our current route and assign it
    * to the currentRoute variable
    */
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    /*
    * TopBar and BottomBar for our application
    * */
    val appTopBar:@Composable () -> Unit = {
        TopAppBar(
            modifier = Modifier.wrapContentSize(),
            title = {
                Text(
                    text = title.value,
                    fontSize = 22.sp,
                    color = Color.White
                )
                Log.d("TopBarCurrentScreen", title.value)
            },
            actions = {
                when (title.value) {
                    "Chats" -> {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.AddComment, contentDescription = "New chat")
                        }
                    }
                    "Contacts" -> {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.PersonAdd, contentDescription = "Add contact")
                        }
                    }
                    else -> {
                        IconButton(
                            onClick = {
                                authViewModel.signOut()
                                when(val result = authViewModel.authResult.value){
                                    is Result.Success -> {
                                        Toast.makeText(context, "Goodbye!", Toast.LENGTH_SHORT).show()
                                        Log.d("IS_SIGNED_IN",
                                            (FIREBASE_AUTH?.currentUser != null).toString()
                                        )
                                        navHostController.navigate(Route.loginRoute.routeToScreen){
                                            popUpTo(0)
                                        }
                                    }
                                    is Result.Error -> {
                                        Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                                    }
                                    //TODO THINK WHAT TO DO WITH NULLABILITY OF AUTHRESULT
                                    null -> {
                                        Toast.makeText(context, "Something gone wrong, tap once more", Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }
                        ) {
                            Icon(imageVector = Icons.Filled.AssistWalker, contentDescription = "Exit")
                        }
                    }
                }
            }
        )
    }

    val appBottomBar:@Composable () -> Unit = {
        if (
            currentScreen == Screen.BottomScreen.Chats ||
            currentScreen == Screen.BottomScreen.Contacts ||
            currentScreen == Screen.BottomScreen.Settings
        ){
            BottomAppBar(
                modifier = Modifier.wrapContentSize()
            ) {
                screensInBottomBar.forEach{ item ->
                    val isSelected = currentRoute == item.bRoute
                    val tint = if (isSelected) Color.White else Color.Black
                    BottomNavigationItem(
                        selected = isSelected,
                        onClick = {
                            navHostController.navigate(item.bRoute){
                                viewModel.setCurrentScreen(item)
                                title.value = item.bTitle
                                Log.d("BottomBarCurrentScreen", currentScreen.title)
                                popUpTo(0)
                            }
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
        topBar = { if(FIREBASE_AUTH?.currentUser != null) {appTopBar()} } ,
        bottomBar = { if(FIREBASE_AUTH?.currentUser != null) {appBottomBar()} },
        scaffoldState = scaffoldState
    ) {
        val pd = it
        NavigationGraph(
            navHostController = navHostController,
            authViewModel = authViewModel,
            userIsSignedIn = FIREBASE_AUTH?.currentUser != null,
            currentUser = currentUser
        )
    }

}
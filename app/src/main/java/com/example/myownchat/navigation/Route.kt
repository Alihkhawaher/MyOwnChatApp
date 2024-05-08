package com.example.myownchat.navigation

sealed class Route(val routeToScreen: String) {
    object loginRoute: Route("loginscreen")
    object signUpRoute: Route("signupscreen")
}
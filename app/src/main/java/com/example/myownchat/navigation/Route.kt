package com.example.myownchat.navigation

sealed class Route(val routeToScreen: String) {
    object loginRoute: Route("loginscreen")
    object signUpRoute: Route("signupscreen")
    object chatsRoute: Route("chatsscreen")

    object settingsRoute: Route("settingsscreen")
    object contactsRoute: Route("contactsscreen")
}
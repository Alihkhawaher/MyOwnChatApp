package com.example.myownchat.data

data class User(
    val email: String = "",
    val login: String = "",
    val image: String = "",
    val isAdmin: Boolean = false
)
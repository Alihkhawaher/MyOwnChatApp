package com.example.myownchat.data

data class User(
    val isAdmin: Boolean = false,
    val email: String = "",
    val image: String = "",
    val login: String = ""
)
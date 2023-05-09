package com.example.newta.model.login

data class LoginUser(
    val user: String,
    val username: String = "",
    val address: String,
    val password: String = "",
    val level: String = "user",
    val status: Int = 0
)

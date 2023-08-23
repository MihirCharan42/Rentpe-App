package com.example.rentpe.models.user.register

data class RegisterRequest(
    val email: String,
    val mobile: String,
    val name: String,
    val password: String
)
package com.example.rentpe.models.user.login

data class LoginResponse(
    val email: String,
    val flag: Boolean,
    val id: Int,
    val jwt_access_token: String,
    val jwt_refresh_token: String,
    val mobile: String,
    val name: String,
    val message: String
)
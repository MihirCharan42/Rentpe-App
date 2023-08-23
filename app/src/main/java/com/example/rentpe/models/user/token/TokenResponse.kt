package com.example.rentpe.models.user.token

data class TokenResponse(
    val flag: Boolean,
    val jwt_access_token: String
)
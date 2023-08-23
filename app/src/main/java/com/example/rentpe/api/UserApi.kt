package com.example.rentpe.api

import com.example.rentpe.utils.Constants.LOGIN_URL
import com.example.rentpe.utils.Constants.NEW_TOKEN
import com.example.rentpe.utils.Constants.REGISTER_URL
import com.example.rentpe.models.user.login.LoginRequest
import com.example.rentpe.models.user.login.LoginResponse
import com.example.rentpe.models.user.register.RegisterRequest
import com.example.rentpe.models.user.register.RegisterResponse
import com.example.rentpe.models.user.token.TokenRequest
import com.example.rentpe.models.user.token.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST(REGISTER_URL)
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST(LOGIN_URL)
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST(NEW_TOKEN)
    suspend fun newToken(@Body tokenRequest: TokenRequest): Response<TokenResponse>
}
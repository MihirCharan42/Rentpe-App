package com.example.rentpe.api

import android.util.Log
import com.example.rentpe.utils.Constants.BASE_URL
import com.example.rentpe.utils.TokenManager
import com.example.rentpe.models.user.token.TokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject

class Interceptor @Inject constructor(): Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var userApi: UserApi

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getAccessToken()

        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")

        val originalResponse: Response = chain.proceed(request.build())
        if(originalResponse.code == 403){
            originalResponse.close()
            Log.e("token status","expired $token")
            val refresh = tokenManager.getRefreshToken()
            runBlocking {
                fetchNewAccessToken(refresh!!)
            }
            val newToken = tokenManager.getAccessToken()
            if(newToken.isNullOrEmpty()){
                return createErrorResponse("Please Login Again")
            }
            val newRequest = chain.request().newBuilder()
            newRequest.addHeader("Authorization", "Bearer $newToken")
            Log.e("token status","new $token")
            return chain.proceed(newRequest.build())
        }
        return originalResponse
    }

    private fun createErrorResponse(message: String): Response {
        val responseBody = ResponseBody.create("text/plain".toMediaTypeOrNull(), message)

        return Response.Builder()
            .code(401)
            .message("Refresh Token is Expired, please login again.")
            .request(Request.Builder().url(BASE_URL).build())
            .protocol(Protocol.HTTP_1_1)
            .body(responseBody)
            .build()
    }

    suspend fun fetchNewAccessToken(refresh: String) {
        val req = CoroutineScope(Dispatchers.IO).async {
            userApi.newToken(TokenRequest(refresh))
        }
        val response = req.await()
        if(!response.isSuccessful || !response.body()!!.flag){
            tokenManager.logout()
            return
        }
        val newAccessToken = response.body()!!.jwt_access_token
        tokenManager.saveToken(newAccessToken, refresh)
    }
}
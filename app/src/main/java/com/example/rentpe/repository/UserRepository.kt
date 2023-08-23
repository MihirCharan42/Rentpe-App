package com.example.rentpe.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.api.UserApi
import com.example.rentpe.models.user.login.LoginRequest
import com.example.rentpe.models.user.login.LoginResponse
import com.example.rentpe.models.user.register.RegisterRequest
import com.example.rentpe.models.user.register.RegisterResponse
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

    private val _loginResponseLiveData = MutableLiveData<NetworkResult<LoginResponse>>()
    val loginResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = _loginResponseLiveData

    private val _registerResponseLiveData = MutableLiveData<NetworkResult<RegisterResponse>>()
    val registerResponseLiveData: LiveData<NetworkResult<RegisterResponse>>
        get() = _registerResponseLiveData

    suspend fun register(registerRequest: RegisterRequest) {
        _registerResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.register(registerRequest)
        if(response.isSuccessful == true || response.body() != null){
            if(response.body()!!.flag == true) {
                Log.e("res =>", response.body().toString())
                _registerResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else {
                _registerResponseLiveData.postValue(NetworkResult.Error(response.body().toString()))
            }
        } else if(response.errorBody() != null){
            _registerResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        } else {
            _registerResponseLiveData.postValue(NetworkResult.Error("Something went Wrong!"))
        }
    }

    suspend fun login(loginRequest: LoginRequest) {
        _loginResponseLiveData.postValue(NetworkResult.Loading())
        val response = userApi.login(loginRequest)
        if(response.isSuccessful == true || response.body() != null){
            if(response.body()!!.flag == true) {
                Log.e("res =>", response.body().toString())
                _loginResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else {
                _loginResponseLiveData.postValue(NetworkResult.Error(response.body()!!.message))
            }
        } else if(response.errorBody() != null){
            _loginResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        } else {
            _loginResponseLiveData.postValue(NetworkResult.Error("Something went Wrong!"))
        }
    }
}
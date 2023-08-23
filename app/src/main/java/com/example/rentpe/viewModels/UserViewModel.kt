package com.example.rentpe.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.models.user.login.LoginRequest
import com.example.rentpe.models.user.login.LoginResponse
import com.example.rentpe.models.user.register.RegisterRequest
import com.example.rentpe.models.user.register.RegisterResponse
import com.example.rentpe.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    val registerResponseLiveData: LiveData<NetworkResult<RegisterResponse>>
        get() = userRepository.registerResponseLiveData

    val loginResponseLiveData: LiveData<NetworkResult<LoginResponse>>
        get() = userRepository.loginResponseLiveData

    fun registerUser(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            userRepository.register(registerRequest)
        }
    }

    fun loginUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            userRepository.login(loginRequest)
        }
    }
}
package com.example.rentpe.activities.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.rentpe.activities.HomeActivity
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.utils.TokenManager
import com.example.rentpe.utils.Helper
import com.example.rentpe.databinding.ActivityLoginBinding
import com.example.rentpe.models.user.login.LoginRequest
import com.example.rentpe.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val userViewModel by viewModels<UserViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.loginResponseLiveData.observe(this@LoginActivity) {
            binding.loader.isVisible = false
            binding.errorMsg.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.jwt_access_token, it.data!!.jwt_refresh_token)
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                is NetworkResult.Error -> {
                    binding.errorMsg.text = it.message
                    binding.errorMsg.isVisible = true
                    binding.loginBtn.isVisible = true
                }
                is NetworkResult.Loading -> {
                    binding.loginBtn.isVisible = false
                    binding.errorMsg.isVisible = false
                    binding.loader.isVisible = true
                }
            }
        }

        binding.loginBtn.setOnClickListener {
            val login_key = binding.loginKeyLogin.text.toString()
            val password = binding.passwordLogin.text.toString()

            val helper = Helper()

            if(!helper.validEmail(login_key) && !helper.validMobile(login_key))
                Toast.makeText(this, "Invalid Email or Mobile", Toast.LENGTH_SHORT).show()
            else if(!helper.validPassword(password))
                Toast.makeText(this, "Empty Password", Toast.LENGTH_SHORT).show()
            else {
                userViewModel.loginUser(LoginRequest(login_key,password))
            }
        }

        binding.goToRegisterBtn.setOnClickListener {
            finish()
        }
    }
}
package com.example.rentpe.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.rentpe.activities.HomeActivity
import com.example.rentpe.utils.Constants.ACCESS_TOKEN
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.utils.TokenManager
import com.example.rentpe.utils.Helper
import com.example.rentpe.databinding.ActivityMainBinding
import com.example.rentpe.models.user.register.RegisterRequest
import com.example.rentpe.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by viewModels<UserViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(tokenManager.prefs.getString(ACCESS_TOKEN, null) != null){
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        userViewModel.registerResponseLiveData.observe(this@MainActivity) {
            binding.loader.isVisible = false
            binding.errorMsg.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                is NetworkResult.Error -> {
                    binding.errorMsg.text = it.message
                    binding.errorMsg.isVisible = true
                    binding.registerBtn.isVisible = true
                }
                is NetworkResult.Loading -> {
                    binding.registerBtn.isVisible = false
                    binding.errorMsg.isVisible = false
                    binding.loader.isVisible = true
                }
            }
        }

        binding.registerBtn.setOnClickListener {
            val email = binding.emailRegister.text.toString()
            val name = binding.nameRegister.text.toString()
            val password = binding.passwordRegister.text.toString()
            val mobile = binding.mobileRegister.text.toString()

            val helper = Helper()

            if(!helper.validEmail(email))
                Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
            else if(!helper.validMobile(mobile))
                Toast.makeText(this, "Invalid Mobile", Toast.LENGTH_SHORT).show()
            else if(!helper.validName(name))
                Toast.makeText(this, "Invalid Name", Toast.LENGTH_SHORT).show()
            else if(!helper.validPassword(password))
                Toast.makeText(this, "Empty Password", Toast.LENGTH_SHORT).show()
            else {
                userViewModel.registerUser(RegisterRequest(email, mobile, name, password))
            }
        }

        binding.goToLoginBtn.setOnClickListener {
            val intent: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
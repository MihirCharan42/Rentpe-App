package com.example.rentpe.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.R
import com.example.rentpe.activities.house.HouseDetails
import com.example.rentpe.activities.transactions.TransactionsActivity
import com.example.rentpe.adapters.HouseAdapter
import com.example.rentpe.utils.TokenManager
import com.example.rentpe.databinding.ActivityHomeBinding
import com.example.rentpe.fragments.HomeFragment
import com.example.rentpe.viewModels.HouseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    lateinit var _binding: ActivityHomeBinding

    private val houseViewModel by viewModels<HouseViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val toolbar: Toolbar = findViewById(R.id.home_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"

        val fragment = HomeFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.house_fragment, fragment)
            .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.logout){
            tokenManager.logout()
        } else if(id == R.id.plus){
            val i = Intent(this@HomeActivity, HouseDetails::class.java)
            startActivity(i)
        } else if(id == R.id.transactions){
            val i = Intent(this@HomeActivity, TransactionsActivity::class.java)
            startActivity(i)
        }
        return true
    }

}


package com.example.rentpe.activities.transactions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentpe.R
import com.example.rentpe.adapters.HouseAdapter
import com.example.rentpe.adapters.TransactionAdapter
import com.example.rentpe.databinding.ActivityTransactionsBinding
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.viewModels.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionsBinding
    private lateinit var adapter: TransactionAdapter
    private val transactionViewModel by viewModels<TransactionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.transactions_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Payments"

        transactionViewModel.getTransaction()
        adapter = TransactionAdapter()


        transactionViewModel.transactionLiveData.observe(this) {
            binding.errorTv.isVisible = false
            binding.progressBarTransactions.isVisible = false
            when(it){
                is NetworkResult.Loading -> {
                    binding.progressBarTransactions.isVisible = true
                    binding.errorTv.isVisible = false
                }
                is NetworkResult.Success -> {
                    binding.progressBarTransactions.isVisible = false
                    binding.errorTv.isVisible = false
                    adapter.submitList(it.data!!.results)
                }
                is NetworkResult.Error -> {
                    binding.progressBarTransactions.isVisible = false
                    binding.errorTv.isVisible = true
                    binding.errorTv.text = it.message
                }
            }
        }

        binding.transactionsRecyclerview.adapter = adapter
        binding.transactionsRecyclerview.layoutManager = LinearLayoutManager(this@TransactionsActivity)

    }
}
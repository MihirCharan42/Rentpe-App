package com.example.rentpe.activities.transactions

import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.rentpe.R
import com.example.rentpe.databinding.ActivityPaymentBinding
import com.example.rentpe.models.transactions.TransactionRequest
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.viewModels.TransactionViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityPaymentBinding
    private val transactionViewModel by viewModels<TransactionViewModel>()

    private lateinit var phone: String
    private lateinit var month: String
    private lateinit var rent: String
    private var id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_payment)

        transactionViewModel.statusLiveData.observe(this@PaymentActivity) {
            binding.loader.isVisible = false
            binding.payTv.isVisible = false
            when(it){
                is NetworkResult.Loading -> {
                    binding.loader.isVisible = true
                    binding.payTv.isVisible = false
                    binding.paymentBtn.isVisible = false
                }
                is NetworkResult.Success -> {
                    binding.loader.isVisible = false
                    binding.payTv.isVisible = true
                    binding.paymentBtn.isEnabled = false
                    binding.payTv.text = "Payment Successful"
                }
                is NetworkResult.Error -> {
                    binding.loader.isVisible = false
                    binding.payTv.isVisible = true
                    binding.paymentBtn.isEnabled = true
                    binding.payTv.text = "Payment Unsuccessful"
                }
            }
        }

        val incomingMessages = intent.extras
        if (incomingMessages != null) {
            phone = incomingMessages.getString("phone") ?: ""
            rent = incomingMessages.getString("rent") ?: ""
            month = incomingMessages.getString("month") ?: ""
            id = incomingMessages.getInt("id") ?: -1
        }

        binding.paymentBtn.setOnClickListener {
            val checkout = Checkout()
            val amt = Integer.parseInt(rent) * 100
            checkout.setKeyID("rzp_test_yFLjZMNjnGOWw2")
            checkout.setImage(R.drawable.ic_launcher_background)
            try {
                val obj = JSONObject()
                obj.put("name", "RentPe")
                obj.put("description", "something")
                obj.put("theme.color", "#0093DD")
                obj.put("currency", "INR")
                obj.put("amount", amt)
                obj.put("prefill.contact", phone)
                checkout.open(this@PaymentActivity, obj)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        binding.payTv.text = "Please pay your rent for the month of $month"

    }

    override fun onPaymentSuccess(p0: String?) {
        transactionViewModel.postTransaction(TransactionRequest(Integer.parseInt(rent), id, p0!!))
        binding.payTv.text = "Payment Successful"
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        binding.payTv.text = "Payment Unsuccessful"
        Toast.makeText(this@PaymentActivity, "Unsuccessful Transaction", Toast.LENGTH_SHORT).show()
    }
}
package com.example.rentpe.models.transactions

data class TransactionRequest(
    val amount: Int,
    val home_id: Int,
    val transaction_id: String
)
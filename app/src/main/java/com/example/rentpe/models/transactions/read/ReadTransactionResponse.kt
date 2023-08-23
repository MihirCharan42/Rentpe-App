package com.example.rentpe.models.transactions.read

data class ReadTransactionResponse(
    val flag: Boolean,
    val message: String,
    val results: List<Transaction>
)
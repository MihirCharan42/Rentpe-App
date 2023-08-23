package com.example.rentpe.models.transactions.read

data class Transaction(
    val amount: Int,
    val landlord_user__name: String,
    val tenant_user__name: String,
    val transaction_id: String,
    val created_at: String,
)
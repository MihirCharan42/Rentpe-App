package com.example.rentpe.models.house.read

data class House(
    val address: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val images: ArrayList<String>,
    val landlord_name: String,
    val landlord_phone: String,
    val last_payment: String,
    val rent: Int,
    val rent_due: String,
    val tenant_name: String,
    val tenant_phone: String,
    val updated_at: String
)

package com.example.rentpe.models.house

import android.net.Uri

data class HouseRequest(
    val address: String,
    val description: String,
    val landlord_name: String,
    val landlord_phone: String,
    val rent: Int,
    val tenant_name: String,
    val tenant_phone: String,
    val images: ArrayList<Uri>
)
package com.example.rentpe.models.house.read

data class ReadHouseResponse(
    val flag: Boolean,
    val results: List<House>,
    val message: String
)
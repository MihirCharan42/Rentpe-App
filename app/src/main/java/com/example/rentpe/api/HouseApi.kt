package com.example.rentpe.api

import com.example.rentpe.models.house.HouseRequest
import com.example.rentpe.models.house.HouseResponse
import com.example.rentpe.models.house.UpdateHouseRequest
import com.example.rentpe.utils.Constants.GET_HOME_URL
import com.example.rentpe.utils.Constants.POST_HOME_URL
import com.example.rentpe.utils.Constants.PUT_HOME_URL
import com.example.rentpe.models.house.read.ReadHouseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HouseApi {

    @GET(GET_HOME_URL)
    suspend fun getHouse(): Response<ReadHouseResponse>

    @POST(POST_HOME_URL)
    suspend fun postHouse(@Body houseRequest: HouseRequest): Response<HouseResponse>

    @PUT(PUT_HOME_URL)
    suspend fun putHouse(@Path("home_id") home_id: Int, @Body updateHouseRequest: UpdateHouseRequest): Response<HouseResponse>
}
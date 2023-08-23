package com.example.rentpe.api

import com.example.rentpe.models.transactions.TransactionRequest
import com.example.rentpe.models.transactions.TransactionResponse
import com.example.rentpe.models.transactions.read.ReadTransactionResponse
import com.example.rentpe.utils.Constants.GET_TRANSACTIONS_URL
import com.example.rentpe.utils.Constants.POST_TRANSACTIONS_URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TransactionApi {

    @GET(GET_TRANSACTIONS_URL)
    suspend fun getTransactions(): Response<ReadTransactionResponse>

    @POST(POST_TRANSACTIONS_URL)
    suspend fun postTransactions(@Body transactionRequest: TransactionRequest): Response<TransactionResponse>
}
package com.example.rentpe.repository

import android.util.Log
import android.view.SurfaceControl.Transaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rentpe.api.TransactionApi
import com.example.rentpe.models.transactions.TransactionRequest
import com.example.rentpe.models.transactions.TransactionResponse
import com.example.rentpe.models.transactions.read.ReadTransactionResponse
import com.example.rentpe.utils.NetworkResult
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val transactionApi: TransactionApi) {

    private val _transactionLiveData = MutableLiveData<NetworkResult<ReadTransactionResponse>>()
    val transactionLiveData: LiveData<NetworkResult<ReadTransactionResponse>>
        get() = _transactionLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<TransactionResponse>>()
    val statusLiveData: LiveData<NetworkResult<TransactionResponse>>
        get() = _statusLiveData

    suspend fun getTransactions() {
        _transactionLiveData.postValue(NetworkResult.Loading())
        val response = transactionApi.getTransactions()
        if(response.isSuccessful || response.body() != null){
            Log.e("Read res =>", response.body().toString())
            if(response.body()!!.flag){
                _transactionLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else {
                _transactionLiveData.postValue(NetworkResult.Error(response.body()!!.message))
            }
        } else if(response.errorBody() != null){
            Log.e("Read error =>", response.errorBody().toString())
            _transactionLiveData.postValue(NetworkResult.Error("Something Went Wrong!"))
        } else {
            _transactionLiveData.postValue(NetworkResult.Error("Something Went Wrong!"))
        }
    }

    suspend fun postTransaction(transactionRequest: TransactionRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = transactionApi.postTransactions(transactionRequest)
        if(response.isSuccessful || response.body() != null){
            Log.e("POST res =>", response.body().toString())
            if(response.body()!!.flag){
                _statusLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else {
                _statusLiveData.postValue(NetworkResult.Error(response.body()!!.message))
            }
        } else if(response.errorBody() != null){
            Log.e("POST error =>", response.errorBody().toString())
            _statusLiveData.postValue(NetworkResult.Error("Something Went Wrong!"))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something Went Wrong!"))
        }

    }

}
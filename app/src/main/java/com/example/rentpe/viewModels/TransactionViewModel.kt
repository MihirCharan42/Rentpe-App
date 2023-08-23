package com.example.rentpe.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentpe.models.transactions.TransactionRequest
import com.example.rentpe.models.transactions.TransactionResponse
import com.example.rentpe.models.transactions.read.ReadTransactionResponse
import com.example.rentpe.repository.TransactionRepository
import com.example.rentpe.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val transactionRepository: TransactionRepository): ViewModel() {

    val transactionLiveData: LiveData<NetworkResult<ReadTransactionResponse>>
        get() = transactionRepository.transactionLiveData

    val statusLiveData: LiveData<NetworkResult<TransactionResponse>>
        get() = transactionRepository.statusLiveData
    fun getTransaction(){
        viewModelScope.launch {
            transactionRepository.getTransactions()
        }
    }

    fun postTransaction(transactionRequest: TransactionRequest){
        viewModelScope.launch {
            transactionRepository.postTransaction(transactionRequest)
        }
    }
}
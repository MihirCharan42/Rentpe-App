package com.example.rentpe.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentpe.models.house.HouseResponse
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.models.house.read.ReadHouseResponse
import com.example.rentpe.models.house.HouseRequest
import com.example.rentpe.models.house.UpdateHouseRequest
import com.example.rentpe.repository.HouseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseViewModel @Inject constructor(private val houseRepository: HouseRepository): ViewModel() {

    val houseReadLiveData: LiveData<NetworkResult<ReadHouseResponse>>
        get() = houseRepository.houseReadLiveData

    val houseStatusLiveData: LiveData<NetworkResult<HouseResponse>>
        get() = houseRepository.houseStatusLiveData

    fun getHomes() {
        viewModelScope.launch {
            houseRepository.getHouse()
        }
    }

    fun postHomes(houseRequest: HouseRequest) {
        viewModelScope.launch {
            houseRepository.postHouse(houseRequest)
        }
    }

    fun putHomes(id: Int, updateHouseRequest: UpdateHouseRequest) {
        viewModelScope.launch {
            houseRepository.putHouse(id, updateHouseRequest)
        }
    }
}
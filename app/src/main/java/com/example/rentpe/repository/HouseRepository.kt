package com.example.rentpe.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.api.HouseApi
import com.example.rentpe.models.house.HouseResponse
import com.example.rentpe.models.house.read.ReadHouseResponse
import com.example.rentpe.models.house.HouseRequest
import com.example.rentpe.models.house.UpdateHouseRequest
import javax.inject.Inject

class HouseRepository @Inject constructor(private val houseApi: HouseApi){

    private val _houseReadLiveData = MutableLiveData<NetworkResult<ReadHouseResponse>>()
    val houseReadLiveData: LiveData<NetworkResult<ReadHouseResponse>>
        get() = _houseReadLiveData

    private val _houseStatusLiveData = MutableLiveData<NetworkResult<HouseResponse>>()
    val houseStatusLiveData: LiveData<NetworkResult<HouseResponse>>
        get() = _houseStatusLiveData

    suspend fun getHouse() {
        _houseReadLiveData.postValue(NetworkResult.Loading())
        val response = houseApi.getHouse()
        if(response.isSuccessful || response.body() != null){
            if(response.body()!!.flag) {
                Log.e("Read res =>", response.body().toString())
                _houseReadLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else {
                _houseReadLiveData.postValue(NetworkResult.Error(response.body()!!.message))
            }
        } else if(response.errorBody() != null){
            Log.e("Read error =>", response.errorBody().toString())
            _houseReadLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        } else {
            _houseReadLiveData.postValue(NetworkResult.Error("Something went Wrong!"))
        }
    }

    suspend fun postHouse(houseRequest: HouseRequest){
        _houseStatusLiveData.postValue(NetworkResult.Loading())
        val response = houseApi.postHouse(houseRequest)
        if(response.isSuccessful || response.body() != null){
            if(response.body()!!.flag){
                Log.e("Create Res =>", response.body().toString())
                _houseStatusLiveData.postValue(NetworkResult.Success(response.body()))
            } else {
                _houseStatusLiveData.postValue(NetworkResult.Error(response.body()!!.message))
            }
        } else if(response.errorBody() != null) {
            Log.e("Create error =>", response.code().toString())
            _houseStatusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        } else {
            _houseStatusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun putHouse(id: Int, updateHouseRequest: UpdateHouseRequest){
        _houseStatusLiveData.postValue(NetworkResult.Loading())
        val response = houseApi.putHouse(id, updateHouseRequest)
        if(response.isSuccessful || response.body() != null){
            if(response.body()!!.flag){
                Log.e("Update Res =>", response.body().toString())
                _houseStatusLiveData.postValue(NetworkResult.Success(response.body()))
            } else {
                _houseStatusLiveData.postValue(NetworkResult.Error(response.body()!!.message))
            }
        } else if(response.errorBody() != null) {
            Log.e("Update error =>", response.errorBody().toString())
            _houseStatusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        } else {
            _houseStatusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}
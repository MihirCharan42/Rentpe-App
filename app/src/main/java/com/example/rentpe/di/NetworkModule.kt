package com.example.rentpe.di

import com.example.rentpe.utils.Constants.BASE_URL
import com.example.rentpe.api.HouseApi
import com.example.rentpe.api.Interceptor
import com.example.rentpe.api.TransactionApi
import com.example.rentpe.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provdeRetrofit(): Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideokHttpClient(interceptor: Interceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    @Singleton
    @Provides
    fun provideUserRetrofit(retrofitBuilder: Retrofit.Builder): UserApi{
        return retrofitBuilder.build().create(UserApi::class.java)
    }


    @Singleton
    @Provides
    fun provideHouseRetrofit(retrofitBuilder: Builder, okHttpClient: OkHttpClient): HouseApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(HouseApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTransactionRetrofit(retrofitBuilder: Builder, okHttpClient: OkHttpClient): TransactionApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(TransactionApi::class.java)
    }
}
package com.example.kodetrainee.api

import com.example.kodetrainee.utils.Constraints.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }


    val api : ApiService by lazy {
        retrofit.create( ApiService::class.java)
    }
}
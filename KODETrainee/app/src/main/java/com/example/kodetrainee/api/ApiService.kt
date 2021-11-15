package com.example.kodetrainee.api

import com.example.kodetrainee.entity.Item
import com.example.kodetrainee.entity.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

    @Headers("Content-Type: application/json", "Prefer: code=200, example=success")
    @GET("users")
    suspend fun getAllUsers(): Response<Item?>
}
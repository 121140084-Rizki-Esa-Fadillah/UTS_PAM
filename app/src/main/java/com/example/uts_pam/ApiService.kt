package com.example.uts_pam

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    fun getUsers(): Call<ApiResponse>
    @GET("users/{userId}")
    fun getUser(@Path("userId") userId: Int): Call<User>
}



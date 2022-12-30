package com.example.githubapp.data.remove

import com.example.githubapp.data.remove.request_first.RetrofitClientInt
import com.example.githubapp.data.remove.request_second.RetrofitClientInt1
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.github.com"
val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(com.example.githubapp.data.remove.BASE_URL)
    .build()
object GitApi {
    val retrofitService: RetrofitClientInt by lazy { com.example.githubapp.data.remove.retrofit.create(
        RetrofitClientInt::class.java) }
}

object GitApi1 {
    val retrofitService1: RetrofitClientInt1 by lazy { com.example.githubapp.data.remove.retrofit.create(
        RetrofitClientInt1::class.java) }
}

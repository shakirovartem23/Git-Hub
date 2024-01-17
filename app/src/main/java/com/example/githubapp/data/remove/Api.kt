package com.example.githubapp.data.remove

import com.example.githubapp.data.remove.request_first.RetrofitClientInt
import com.example.githubapp.data.remove.request_second.RetrofitClientInt1
import com.example.githubapp.data.remove.request_second.RetrofitClientInt2
import com.example.githubapp.data.remove.request_second.RetrofitClientInt3
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://api.github.com"
var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
var client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(logging)
    .addInterceptor {
        val request = it.request().newBuilder().addHeader("Authorization", "Bearer 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").build()
        it.proceed(request)
    }
    .build()
val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(com.example.githubapp.data.remove.BASE_URL)
    .client(client)
    .build()!!
object GitApi {
    val retrofitService: RetrofitClientInt by lazy { com.example.githubapp.data.remove.retrofit.create(
        RetrofitClientInt::class.java) }
}
object GitApi1 {
    val retrofitService1: RetrofitClientInt1 by lazy { com.example.githubapp.data.remove.retrofit.create(
        RetrofitClientInt1::class.java) }
}
object GitApi2 {
    val retrofitService2: RetrofitClientInt2 by lazy { com.example.githubapp.data.remove.retrofit.create(
        RetrofitClientInt2::class.java) }
}
object GitApi3 {
    val retrofitService3: RetrofitClientInt3 by lazy { com.example.githubapp.data.remove.retrofit.create(
        RetrofitClientInt3::class.java) }
}

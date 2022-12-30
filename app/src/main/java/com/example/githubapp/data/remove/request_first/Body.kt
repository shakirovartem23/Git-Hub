package com.example.githubapp.data.remove.request_first

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitClientInt {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Call<List<Repo>>
}

data class Repo(val id: String, val name: String, val stargazers_count: Int) {
}
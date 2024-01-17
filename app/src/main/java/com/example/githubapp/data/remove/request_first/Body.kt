package com.example.githubapp.data.remove.request_first

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitClientInt {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String, @Query("page") page: Int): List<Repo>
}

data class Repo(
    val id: Int,
    val name: String,
    val stargazers_count: Int,
    val owner: Owner
)

data class Owner(
    val id: Int,
    val login: String,
    val avatar_url: String
)
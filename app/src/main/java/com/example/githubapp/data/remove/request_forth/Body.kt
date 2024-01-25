package com.example.githubapp.data.remove.request_forth

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RetrofitClientInt3 {
    @GET("users/{user}")
    suspend fun listRepos(@Path("user") user: String): Repo3
}

data class Repo3(
    val public_repos: Int
)

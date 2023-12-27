package com.example.githubapp.data.remove.request_second

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RetrofitClientInt2 {
    @GET("repos/{user}/{repo}")
    suspend fun listRepos(@Path("user") user: String, @Path("repo") repo: String): Repo2
}

data class Repo2(
    val stargazers_count: Int
)

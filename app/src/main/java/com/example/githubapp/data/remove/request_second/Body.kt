package com.example.githubapp.data.remove.request_second

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RetrofitClientInt1 {
    @GET("repos/{user}/{repo}/stargazers?per_page=100")
    @Headers("Accept: application/vnd.github.star+json")
    suspend fun listRepos1(@Path("user") user: String, @Path("repo") repo: String): List<Repo1>
}

data class Repo1(
    val starred_at: String,
    val user: NewRepo,
)
data class NewRepo(
    val id: Int,
    val login: String,
    val avatar_url: String,
)

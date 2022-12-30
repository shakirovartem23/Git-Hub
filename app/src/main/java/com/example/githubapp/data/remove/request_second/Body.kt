package com.example.githubapp.data.remove.request_second

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface RetrofitClientInt1 {
    @GET("repos/{user}/{repo}/stargazers")
    @Headers("Accept: application/vnd.github.star+json")
    fun listRepos1(@Path("user") user: String, @Path("repo") repo: String): Call<List<Repo1>>
}

data class Repo1(val starred_at: String){
}
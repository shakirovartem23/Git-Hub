package com.example.githubapp.Saved

import com.example.githubapp.data.remove.GitApi
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.data.remove.request_second.Repo1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class SaveDataForSelect {

    suspend fun loadUsersOfStarring(userName: String, repoName: String): List<Repo1>{
        return suspendCoroutine { result ->
            GlobalScope.launch(Dispatchers.Main) {
                val listStar: Call<List<Repo1>> =
                    GitApi1.retrofitService1.listRepos1(userName, repoName)
                listStar.enqueue(object : Callback<List<Repo1>> {
                    override fun onResponse(
                        call: Call<List<Repo1>>,
                        response: Response<List<Repo1>>
                    ) {
                        val body = response.body()
                        if (body != null) {
                            result.resume(body)
                        }
                    }

                    override fun onFailure(call: Call<List<Repo1>>, t: Throwable) {
                    }

                })
            }
        }
    }

    suspend fun loadNameRepos(userName: String): List<Repo>  {
        return suspendCoroutine { result ->
            GlobalScope.launch(Dispatchers.Main) {
                val listRepos: Call<List<Repo>> = GitApi.retrofitService.listRepos("$userName")
                listRepos.enqueue(object : Callback<List<Repo>> {
                    override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                        val body = response.body()
                        if (body != null) {
                            result.resume(body)
                        }
                    }

                    override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
        }
    }
}
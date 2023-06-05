package com.example.githubapp.Saved

import com.example.githubapp.data.remove.GitApi
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.data.remove.request_second.Repo1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SaveDataForSelect() {

    val resultNameRepos = mutableListOf<String>()
    val resultTimeStarring = mutableMapOf<String, String>()

    fun loadTimeStarring(userName: String, repoName: String){
        val listStar: Call<List<Repo1>> = GitApi1.retrofitService1.listRepos1(userName, repoName)
        runBlocking {
            listStar.enqueue(object : Callback<List<Repo1>> {
                override fun onResponse(call: Call<List<Repo1>>, response: Response<List<Repo1>>) {
                    resultTimeStarring.clear()
                    val body = response.body()
                    if (body != null) {
                        for (i in body) {
                            resultTimeStarring[i.starred_at] = i.user.login
                        }
                    }
                }

                override fun onFailure(call: Call<List<Repo1>>, t: Throwable) {
                    throw Exception("Error call retrofit - Map<String, String>, SaveData 18")
                }

            })
        }
    }
    suspend fun loadNameRepos(nameUser: String) = coroutineScope {
        val listRepos: Call<List<Repo>> = GitApi.retrofitService.listRepos("$nameUser")
        listRepos.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                resultNameRepos.clear()
                val body = response.body()
                if (body != null) {
                    for (i in body) {
                        resultNameRepos += i.name
                    }
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
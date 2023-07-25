package com.example.githubapp.Saved

import Save_Data.Star
import com.example.githubapp.data.remove.GitApi
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.data.remove.request_second.Repo1
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface returnResult{
    fun returnNameRepos(resultNameRepos: List<String>)
    fun returnTimeStarring(resultTimeStarring: MutableMap<String, String>)
    fun returnUsersOfStarring(resultUsersOfStarring: MutableList<Triple<String, String, String>>)
}

fun loadUsersOfStarring(
    userName: String,
    repoName: String,
    dataValue: List<String>,
    employees: List<Star>,
    callback: returnResult
) {
    val listRepos = mutableListOf<Triple<String, String, String>>()
    val resultUsersOfStarring = mutableListOf<Triple<String, String, String>>()

    employees.forEach {
        resultUsersOfStarring += Triple(
            it.userId.name,
            it.date.substring(0..9),
            it.date.substring(11..18)
        )
    }

    if(resultUsersOfStarring.isNotEmpty()) {
        val list = mutableListOf<Triple<String, String, String>>()
        resultUsersOfStarring.forEach{
            if(it.first in dataValue){
                list+=it
            }
        }
        callback.returnUsersOfStarring(list)
    } else {
        val listStar: Call<List<Repo1>> = GitApi1.retrofitService1.listRepos1(userName, repoName)
        listStar.enqueue(object : Callback<List<Repo1>> {
            override fun onResponse(call: Call<List<Repo1>>, response: Response<List<Repo1>>) {
                val body = response.body()
                println("Body:$body")
                if (body != null) {
                    for (i in body) {
                        listRepos += Triple(
                            i.user.login,
                            i.starred_at.substring(0..9),
                            i.starred_at.substring(11..18)
                        )
                    }
                }

                listRepos.forEach {
                    if (it.first in dataValue) {
                        resultUsersOfStarring += it
                    }
                }

                callback.returnUsersOfStarring(resultUsersOfStarring)

            }

            override fun onFailure(call: Call<List<Repo1>>, t: Throwable) {
            }

        })
    }
}
fun loadTimeStarring(userName: String, repoName: String, employees: List<Star>, callback: returnResult){
    val resultTimeStarring = mutableMapOf<String, String>()

    employees.forEach {
        resultTimeStarring[it.date] = it.userId.name
    }

    if(resultTimeStarring.isNotEmpty()) {
        callback.returnTimeStarring(resultTimeStarring)
    } else{
        val listStar: Call<List<Repo1>> = GitApi1.retrofitService1.listRepos1(userName, repoName)
        listStar.enqueue(object : Callback<List<Repo1>> {
            override fun onResponse(call: Call<List<Repo1>>, response: Response<List<Repo1>>) {

                val body = response.body()
                if (body != null) {
                    for (i in body) {
                        resultTimeStarring[i.starred_at] = i.user.login
                    }
                }

                callback.returnTimeStarring(resultTimeStarring)
            }

            override fun onFailure(call: Call<List<Repo1>>, t: Throwable) {
                throw Exception("Error call retrofit - Map<String, String>, SaveData 18")
            }

        })
    }
}
@DelicateCoroutinesApi
fun loadNameRepos(userName: String, employees: List<Star>, callback: returnResult){
    val resultNameRepos = mutableListOf<String>()

    employees.forEach {
        resultNameRepos += it.repositoryId.name
    }

    if(resultNameRepos.isNotEmpty()) {
        callback.returnNameRepos(resultNameRepos.sorted())
    }

    val listRepos: Call<List<Repo>> = GitApi.retrofitService.listRepos(userName)
    listRepos.enqueue(object : Callback<List<Repo>> {
        override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {

            val body = response.body()
            if (body != null) {
                for (i in body) {
                    resultNameRepos += i.name
                }
            }

            callback.returnNameRepos(resultNameRepos)
        }

        override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
            t.printStackTrace()
        }
    })
}
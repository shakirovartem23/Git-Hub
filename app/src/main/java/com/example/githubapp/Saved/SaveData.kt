package com.example.githubapp.Saved

import Save_Data.Star
import com.example.githubapp.data.remove.GitApi
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.data.remove.request_second.Repo1
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.DelicateCoroutinesApi

interface returnResult{
    fun returnNameRepos(resultNameRepos: List<String>)
    fun returnTimeStarring(resultTimeStarring: MutableMap<String, String>)
    fun returnUsersOfStarring(resultUsersOfStarring: MutableList<Triple<String, String, String>>)
    fun onClick(starButton: MaterialButton,
                repoName: String
    )
}

suspend fun loadUsersOfStarring(
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
        val listStar: List<Repo1> = try {
            GitApi1.retrofitService1.listRepos1(userName, repoName)
        } catch(e: retrofit2.HttpException) {
            emptyList<Repo1>()
        }
        listStar.forEach {
            listRepos += Triple(
                it.user.login,
                it.starred_at.substring(0..9),
                it.starred_at.substring(11..18)
            )
        }
        listRepos.forEach {
            if (it.first in dataValue) {
                resultUsersOfStarring += it
            }
        }
        callback.returnUsersOfStarring(listRepos)
    }
}
suspend fun loadTimeStarring(userName: String, repoName: String, employees: List<Star>, callback: returnResult){
    val resultTimeStarring = mutableMapOf<String, String>()

    employees.forEach {
        resultTimeStarring[it.date] = it.userId.name
    }

    if(resultTimeStarring.isNotEmpty()) {
        callback.returnTimeStarring(resultTimeStarring)
    } else{
        val listStar: List<Repo1> = try {
            GitApi1.retrofitService1.listRepos1(userName, repoName)
        } catch(e: retrofit2.HttpException) {
            emptyList()
        }
        listStar.forEach {
            resultTimeStarring[it.starred_at] = it.user.login
        }
    }
}
@DelicateCoroutinesApi
suspend fun loadNameRepos(userName: String, employees: List<Star>, callback: returnResult){
    val resultNameRepos = mutableListOf<String>()

    employees.forEach {
        resultNameRepos += it.repositoryId.name
    }

    if(resultNameRepos.isNotEmpty()) {
        callback.returnNameRepos(resultNameRepos.sorted())
    }

    val listRepos: List<Repo> = try {
        GitApi.retrofitService.listRepos(userName)
    } catch(e: retrofit2.HttpException) {
        emptyList()
    }
    listRepos.forEach{ i->
            resultNameRepos += i.name
    }
    callback.returnNameRepos(resultNameRepos)
}
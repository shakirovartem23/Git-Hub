package com.example.githubapp.Saved

import Save_Data.Repository
import Save_Data.Star
import com.example.githubapp.data.remove.GitApi
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.data.remove.request_second.Repo1
import kotlinx.coroutines.DelicateCoroutinesApi

suspend fun loadUsersOfStarring(
    userName: String,
    repoName: String,
    dataValue: List<String>,
    employees: List<Star>
): MutableList<Triple<String, String, String>> {
    val listRepos = mutableListOf<Triple<String, String, String>>()
    val resultUsersOfStarring = mutableListOf<Triple<String, String, String>>()

    employees.forEach {
        resultUsersOfStarring += Triple(
            it.userName,
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
        return listRepos
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
    }
    return resultUsersOfStarring
}
suspend fun loadTimeStarring(userName: String, repoName: String, employees: List<Star>): MutableMap<String, String> {
    val resultTimeStarring = mutableMapOf<String, String>()

    val listStar: List<Repo1> = try {
        GitApi1.retrofitService1.listRepos1(userName, repoName)
    } catch(e: retrofit2.HttpException) {
        emptyList()
    }
    listStar.forEach {
        resultTimeStarring[it.starred_at] = it.user.login
    }

    if(resultTimeStarring.isNotEmpty()) {
        return resultTimeStarring
    } else{
        employees.forEach {
            resultTimeStarring[it.date] = it.userName
        }
    }
    return resultTimeStarring
}
@DelicateCoroutinesApi
suspend fun loadNameRepos(userName: String, employees: List<Repository>): MutableList<String> {
    val resultNameRepos = mutableListOf<String>()

    val listRepos: List<Repo> = try {
        GitApi.retrofitService.listRepos(userName)
    } catch(e: retrofit2.HttpException) {
        emptyList()
    }

    listRepos.forEach{ i->
        resultNameRepos += i.name
    }

    if(resultNameRepos.isNotEmpty()) {
        return resultNameRepos
    }

    employees.forEach {
        resultNameRepos += it.name
    }
    return resultNameRepos
}
package com.example.githubapp.Saved

import Save_Data.Repository
import Save_Data.Star
import com.example.githubapp.data.remove.GitApi
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.GitApi2
import com.example.githubapp.data.remove.GitApi3
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.data.remove.request_second.Repo1
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun generate(count: Int): Int{
    if(count%30==0){
        return count/30
    } else if(count<30){
        return 1
    } else{
        return count/30+1
    }
}

suspend fun loadUsersOfStarring(
    userName: String,
    repoName: String,
    dataValue: List<String>,
    employees: List<Star>
): MutableList<Triple<String, String, String>>  = withContext(Dispatchers.Main){
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
        return@withContext listRepos
    } else {
        val listStar: MutableList<Repo1>  = mutableListOf()
        try {
            val countStar = GitApi2.retrofitService2.listRepos(userName, repoName)
            for (i in 1..generate(countStar.stargazers_count)) {
                listStar += GitApi1.retrofitService1.listRepos1(userName, repoName, i)
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
            return@withContext resultUsersOfStarring
        } catch(e: retrofit2.HttpException) {
            return@withContext resultUsersOfStarring
        }
    }
}
suspend fun loadTimeStarring(userName: String, repoName: String, employees: List<Star>): MutableMap<String, String> = withContext(Dispatchers.Main){
    val resultTimeStarring = mutableMapOf<String, String>()

    employees.forEach {
        resultTimeStarring[it.date] = it.userName
    }

    if(resultTimeStarring.isNotEmpty()) {
        return@withContext resultTimeStarring
    }

    var listStar: MutableList<Repo1> = mutableListOf()
    try {
        val countStar = GitApi2.retrofitService2.listRepos(userName, repoName)
        for (i in 1..generate(countStar.stargazers_count)) {
            listStar += GitApi1.retrofitService1.listRepos1(userName, repoName, i)
        }
        listStar.forEach {
            resultTimeStarring[it.starred_at] = it.user.login
        }
        return@withContext resultTimeStarring
    } catch(e: retrofit2.HttpException) {
        return@withContext resultTimeStarring
    }
}
@DelicateCoroutinesApi
suspend fun loadNameRepos(userName: String, employees: List<Repository>): MutableMap<String, Int> = withContext(Dispatchers.Main){

    val resultNameRepos = mutableMapOf<String, Int>()

    employees.forEach {
        resultNameRepos[it.name] = it.stargazers_count
    }

    if(resultNameRepos.isNotEmpty()) {
        return@withContext resultNameRepos
    }

    var listRepos: MutableList<Repo> = mutableListOf()
    try {
        val countRepo = GitApi3.retrofitService3.listRepos(userName)
        for (i in 1..generate(countRepo.public_repos)) {
            listRepos += GitApi.retrofitService.listRepos(userName, i)
        }
        listRepos.forEach{
            resultNameRepos[it.name] = it.stargazers_count
        }
        return@withContext resultNameRepos
    } catch(e: Exception) {
        e.printStackTrace()
        return@withContext resultNameRepos
    }
}
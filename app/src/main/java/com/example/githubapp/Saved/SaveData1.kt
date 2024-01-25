package com.example.githubapp.Saved

import com.example.githubapp.data.remove.GitApi
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.GitApi2
import com.example.githubapp.data.remove.GitApi3
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.data.remove.request_second.Repo1
import com.example.githubapp.data.remove.request_third.Repo2


@Suppress("UNREACHABLE_CODE")
class SaveDataForSelect {

    suspend fun loadUsersOfStarring(userName: String, repoName: String): List<Repo1> {
        var listUsers: List<Repo1> = listOf()
        try {
            val countStar = GitApi2.retrofitService2.listRepos(userName, repoName)
            for (i in 0 until generate(countStar.stargazers_count)) {
                listUsers += GitApi1.retrofitService1.listRepos(userName, repoName, i + 1)
            }
            return listUsers
        } catch (e: retrofit2.HttpException) {
            return emptyList()
            e.run { printStackTrace() }
        }
    }

    suspend fun loadNameRepos(userName: String): List<Repo> {
        var listRepos: List<Repo> = listOf()
        try {
            val countRepo = GitApi3.retrofitService3.listRepos(userName)
            for (i in 0 until generate(countRepo.public_repos)) {
                listRepos += GitApi.retrofitService.listRepos(userName, i + 1)
            }
            return listRepos
        } catch (e: Exception) {
            return emptyList()
            e.run { printStackTrace() }
        }
    }

    suspend fun loadLikeRepo(userName: String, repoName: String): Repo2? {
        try {
            return GitApi2.retrofitService2.listRepos(userName, repoName)
        } catch (e: retrofit2.HttpException) {
            return null
            e.run { printStackTrace() }
        }
    }
}
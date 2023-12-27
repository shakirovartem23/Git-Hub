package com.example.githubapp.Saved

import com.example.githubapp.data.remove.GitApi
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.GitApi2
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.data.remove.request_second.Repo1
import com.example.githubapp.data.remove.request_second.Repo2


@Suppress("UNREACHABLE_CODE")
class SaveDataForSelect {

    suspend fun loadUsersOfStarring(userName: String, repoName: String): List<Repo1>{
        try {
            return GitApi1.retrofitService1.listRepos1(userName, repoName)
        } catch(e: retrofit2.HttpException) {
            return emptyList()
            e.run { printStackTrace() }
        }
    }

    suspend fun loadNameRepos(userName: String): List<Repo> {
        try {
            return GitApi.retrofitService.listRepos(userName)
        } catch(e: retrofit2.HttpException) {
            return emptyList()
            e.run { printStackTrace() }
        }
    }

    suspend fun loadLikeRepo(userName: String, repoName: String): Repo2? {
        try {
            return GitApi2.retrofitService2.listRepos(userName, repoName)
        } catch(e: retrofit2.HttpException) {
            return null
            e.run { printStackTrace() }
        }
    }
}
package com.example.githubapp.Saved

import com.example.githubapp.data.remove.GitApi
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.data.remove.request_second.Repo1


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
}
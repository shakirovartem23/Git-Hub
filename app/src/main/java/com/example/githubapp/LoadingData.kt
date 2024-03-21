package com.example.githubapp

import Save_Data.AppDatabase
import Save_Data.Repository
import Save_Data.Star
import Save_Data.User
import android.app.Service
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.IBinder
import androidx.room.Room
import com.example.githubapp.Saved.SaveDataForSelect
import com.example.githubapp.Saved.loadUsersOfStarring
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingData : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int  {
        val employeeDao = Room.databaseBuilder(
            this@LoadingData,
            AppDatabase::class.java, "Star"
        ).build().employeeDao()
        val ownerName = intent!!.getStringExtra("ownerName")!!
        val classResult = SaveDataForSelect()

        GlobalScope.launch(Dispatchers.Main) {

            val listRepos = classResult.loadNameRepos(ownerName)
            for (repo in listRepos) {
                if (employeeDao.selectRepo(repo.name) == null) {
                    employeeDao.insertRepo(
                        Repository(
                            0,
                            repo.name,
                            repo.owner.login,
                            repo.stargazers_count,
                            false
                        )
                    )
                }
                for (star in classResult.loadUsersOfStarring(ownerName, repo.name)) {
                    if (employeeDao.selectStar(ownerName, repo.name) == null) {
                        employeeDao.insertStar(
                            Star(
                                0,
                                star.starred_at,
                                star.user.login,
                                repo.name
                            )
                        )
                    }
                    if (employeeDao.selectUser(ownerName) == null) {
                        employeeDao.insertUser(
                            User(
                                0,
                                star.user.login,
                                star.user.avatar_url
                            )
                        )
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}
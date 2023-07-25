package com.example.githubapp

import Save_Data.AppDatabase
import Save_Data.Repository
import Save_Data.Star
import Save_Data.User
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.room.Room
import com.example.githubapp.Saved.SaveDataForSelect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

var id = 0

class interactionWithGitHub : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this@interactionWithGitHub, "Loading...", Toast.LENGTH_LONG).show()
        println("Success")
        GlobalScope.launch(Dispatchers.Main) {
            val employeeDao = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "allStar"
            ).build().employeeDao()
            val listOfStar = employeeDao.allStar()
            val classResult = SaveDataForSelect()
            val userName = intent!!.getStringExtra("userName")!!

            classResult.loadNameRepos(userName).forEach { repo ->

                        classResult.loadUsersOfStarring(userName, repo.name)
                            .forEach { star ->
                                val objectOfStar = Star(
                                    star.starred_at.hashCode() + repo.owner.id.hashCode() + star.user.avatar_url.hashCode() + repo.id.hashCode() + repo.name.hashCode(),
                                    star.starred_at,
                                    User(
                                        repo.owner.id,
                                        star.user.login,
                                        star.user.avatar_url
                                    ),
                                    Repository(
                                        repo.id,
                                        repo.name,
                                        User(
                                            repo.owner.id,
                                            repo.owner.login,
                                            repo.owner.avatar_url
                                        )
                                    )
                                )
                                if (objectOfStar !in listOfStar) {
                                    GlobalScope.launch(Dispatchers.IO) {
                                        employeeDao.insertOfStars(
                                            objectOfStar
                                        )
                                    }
                                }
                            }
                    }
        }
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }
}
package com.example.githubapp.ui.users

import Save_Data.AppDatabase
import Save_Data.AppDatabase1
import Save_Data.Repository
import Save_Data.Star
import Save_Data.User
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.githubapp.R
import com.example.githubapp.Saved.loadUsersOfStarring
import com.example.githubapp.Saved.returnResult
import com.example.githubapp.ui.repo.RepoActivity
import com.example.githubapp.ui.select_repo.toStar1
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UsersActivity: AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users)

        findViewById<Toolbar>(R.id.head1).setOnClickListener {
            val intent = Intent(this@UsersActivity, RepoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        val dataValue = intent.getStringArrayExtra("DataValue")!!.toList()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        val repoName = intent.getStringExtra("title")!!
        val userName = intent.getStringExtra("user")!!

        val employeeDao = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "allStar"
        ).build().employeeDao()

        val employeeDao1 = Room.databaseBuilder(
            applicationContext,
            AppDatabase1::class.java, "favouriteStar"
        ).build().employeeDao1()

        val star3 = findViewById<MaterialButton>(R.id.star4)
        GlobalScope.launch(Dispatchers.Main) {
            val bool = when (employeeDao1.allLikeStar()
                .find { it.repositoryId1.name1 == repoName }) {
                null -> false
                else -> true
            }
            if (bool) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    star3.foregroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.white))
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    star3.foregroundTintList =
                        ColorStateList.valueOf(resources.getColor(R.color.black))
                }
            }
        }

        star3.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val foregroundTint = star3.foregroundTintList
                val allStar = employeeDao.selectStarOfRepo(repoName)
                var listOfStar1 = mutableListOf<Star>()
                if (ColorStateList.valueOf(resources.getColor(R.color.white)) == foregroundTint
                ) {
                    allStar.forEach {
                        val OS0 = Star(
                            0,
                            it.date,
                            User(
                                it.userId.id,
                                it.userId.name,
                                it.userId.avatarUrl
                            ),
                            Repository(
                                it.repositoryId.id,
                                it.repositoryId.name,
                                User(
                                    it.userId.id,
                                    it.userId.name,
                                    it.userId.avatarUrl
                                )
                            )
                        )
                        listOfStar1 += OS0
                    }
                    GlobalScope.launch(Dispatchers.Main) {
                        star3.foregroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.black))
                        employeeDao1.allLikeStar()
                            .filter { it.repositoryId1.name1 == repoName }
                            .forEach {
                                employeeDao1.deleteOfStar(it)
                            }
                    }
                } else {
                    allStar.forEach {
                        val OS0 = Star(
                            0,
                            it.date,
                            User(
                                it.userId.id,
                                it.userId.name,
                                it.userId.avatarUrl
                            ),
                            Repository(
                                it.repositoryId.id,
                                it.repositoryId.name,
                                User(
                                    it.userId.id,
                                    it.userId.name,
                                    it.userId.avatarUrl
                                )
                            )
                        )
                        listOfStar1 += OS0
                    }
                    GlobalScope.launch(Dispatchers.Main) {
                        val list = listOfStar1.map { it.toStar1(it) }
                        star3.foregroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.white))
                        if (employeeDao1.allLikeStar()
                                .filter { it.repositoryId1.name1 == repoName }.isNotEmpty()
                        ) else {
                            list.forEach{
                                employeeDao1.insertOfStar(it)
                            }
                        }
                    }
                }
            }
        }

        val callback = object: returnResult {
            override fun returnNameRepos(resultNameRepos: List<String>) {

            }

            override fun returnTimeStarring(resultTimeStarring: MutableMap<String, String>) {

            }

            override fun returnUsersOfStarring(resultUsersOfStarring: MutableList<Triple<String, String, String>>) {
                recyclerView.layoutManager = LinearLayoutManager(this@UsersActivity)
                recyclerView.adapter = UsersAdapter(resultUsersOfStarring)
            }

            override fun onClick(starButton: MaterialButton,
                                 repoName: String
            ) {
                TODO("Not yet implemented")
            }
        }

        title = repoName
        GlobalScope.launch(Dispatchers.Main) {
            val employees = employeeDao.selectStarOfRepo(repoName)

            GlobalScope.launch(Dispatchers.Main) {
                loadUsersOfStarring(userName, repoName, dataValue, employees, callback)
            }
        }

    }
}
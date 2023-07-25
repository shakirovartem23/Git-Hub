package com.example.githubapp.ui.users

import Save_Data.AppDatabase
import Save_Data.Repository
import Save_Data.Star
import Save_Data.User
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.githubapp.R
import com.example.githubapp.Saved.SaveDataForSelect
import com.example.githubapp.Saved.loadUsersOfStarring
import com.example.githubapp.Saved.returnResult
import com.example.githubapp.ui.repo.RepoActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UsersActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users)

        findViewById<Toolbar>(R.id.head1).setOnClickListener {
            val intent = Intent(this@UsersActivity, RepoActivity::class.java)
            startActivity(intent)
        }

        val dataValue = intent.getStringArrayExtra("DataValue")!!.toList()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        val repoName = intent.getStringExtra("title")!!
        val userName = intent.getStringExtra("user")!!
        val classResult = SaveDataForSelect()

        val employeeDao = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build().employeeDao()

        val callback = object: returnResult {
            override fun returnNameRepos(resultNameRepos: List<String>) {

            }

            override fun returnTimeStarring(resultTimeStarring: MutableMap<String, String>) {

            }

            override fun returnUsersOfStarring(resultUsersOfStarring: MutableList<Triple<String, String, String>>) {
                recyclerView.layoutManager = LinearLayoutManager(this@UsersActivity)
                recyclerView.adapter = UsersAdapter(resultUsersOfStarring)
                GlobalScope.launch(Dispatchers.Main) {
                    val listOfStar = employeeDao.allStar()

                    GlobalScope.launch(Dispatchers.Main) {
                        classResult.loadNameRepos(userName).forEach { repo ->
                            GlobalScope.launch(Dispatchers.Main) {
                                classResult.loadUsersOfStarring(userName, repo.name)
                                    .forEach { star ->
                                        val objectOfStar = Star(
                                            star.starred_at.hashCode(),
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
                                        if(objectOfStar !in listOfStar) {
                                            GlobalScope.launch(Dispatchers.Main) {
                                                employeeDao.insertOfStars(
                                                    objectOfStar
                                                )
                                            }
                                        }
                                    }
                            }
                        }
                    }
                }
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
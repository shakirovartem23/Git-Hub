package com.example.githubapp.ui.select_repo

import Save_Data.AppDatabase
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.githubapp.R
import com.example.githubapp.Saved.loadNameRepos
import com.example.githubapp.Saved.returnResult
import com.example.githubapp.ui.repo.RepoActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@DelicateCoroutinesApi
class SelectRepoActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_repo_activity)

        val intent = Intent(this@SelectRepoActivity, RepoActivity::class.java)
        val floatButton = findViewById<FloatingActionButton>(com.example.githubapp.R.id.floatButton)
        val userName = findViewById<EditText>(R.id.layoutEditText)
        val recyclerView: RecyclerView = findViewById(com.example.githubapp.R.id.recyclerView)

        val employeeDao = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build().employeeDao()
        


        val callback = object: returnResult{
            override fun returnNameRepos(resultNameRepos: List<String>) {
                recyclerView.layoutManager = LinearLayoutManager(this@SelectRepoActivity)
                recyclerView.adapter = SelectRepoAdapter(resultNameRepos.toSet().toList()) {
                    intent.putExtra("title", it)
                    intent.putExtra("user", userName.text.toString())
                    intent.putExtra("resultNameRepos", resultNameRepos.toTypedArray())
                    startActivity(intent)
                }
            }

            override fun returnTimeStarring(resultTimeStarring: MutableMap<String, String>) {

            }

            override fun returnUsersOfStarring(resultUsersOfStarring: MutableList<Triple<String, String, String>>) {

            }

        }

        floatButton.setOnClickListener{
            GlobalScope.launch(Dispatchers.Main) {
                loadNameRepos(
                    userName.text.toString(),
                    employeeDao.selectNameRepos(userName.text.toString()),
                    callback
                )
            }
        }
    }
}

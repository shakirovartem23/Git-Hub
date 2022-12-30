package com.example.githubapp.ui.users

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.request_second.Repo1
import com.example.githubapp.R
import com.example.githubapp.ui.select_repo.RepoAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users)
        var listRepos = mutableListOf<Triple<String, String, Int>>()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        val title = intent.getStringExtra("title")!!
        val user = intent.getStringExtra("user")!!

        setTitle(title)

        val listStar: Call<List<Repo1>> = GitApi1.retrofitService1.listRepos1(user, title)
        listStar.enqueue(object : Callback<List<Repo1>> {
            override fun onResponse(call: Call<List<Repo1>>, response: Response<List<Repo1>>) {
                val body = response.body()

                if (body != null) {
                    for (i in body){
                        listRepos+=Triple()
                    }
                }

                recyclerView.layoutManager = LinearLayoutManager(this@UsersActivity)
                recyclerView.adapter = UsersAdapter(listRepos)
            }

            override fun onFailure(call: Call<List<Repo1>>, t: Throwable) {
            }

        })
    }
}
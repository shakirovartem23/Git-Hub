package com.example.githubapp.ui.users

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.data.remove.GitApi1
import com.example.githubapp.data.remove.request_second.Repo1
import com.example.githubapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users)
        var listRepos = mutableListOf<Triple<String, String, String>>()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        val title = intent.getStringExtra("title")!!
        val ses = when(intent.getIntExtra("Season", 1)){
            2 ->3..5
            3 ->6..8
            4 ->9..11
            else -> listOf(12, 1, 2)
        }
        val listRepos1 = mutableListOf<Triple<String, String, String>>()

        setTitle(title)

        val listStar: Call<List<Repo1>> = GitApi1.retrofitService1.listRepos1(intent.getStringExtra("user")!!, title)
        listStar.enqueue(object : Callback<List<Repo1>> {
            override fun onResponse(call: Call<List<Repo1>>, response: Response<List<Repo1>>) {
                val body = response.body()
                println("Error:$body")
                if (body != null) {
                    for (i in body){
                        listRepos+=Triple(i.user.login, i.starred_at.substring(0..9), i.starred_at.substring(11..18))
                    }
                }

                listRepos.forEach {
                    if(it.second.substring(5..6).toInt() in ses){
                        listRepos1+=it
                    }
                }

                recyclerView.layoutManager = LinearLayoutManager(this@UsersActivity)
                recyclerView.adapter = UsersAdapter(listRepos1)
            }

            override fun onFailure(call: Call<List<Repo1>>, t: Throwable) {
            }

        })
    }
}
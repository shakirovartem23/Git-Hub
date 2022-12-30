package com.example.githubapp.ui.select_repo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.data.remove.GitApi.retrofitService
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.ui.repo.RepoActivity
import com.example.githubapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class SelectRepoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_repo_activity)

        println("Error 2")

        val floatButton = findViewById<FloatingActionButton>(R.id.floatButton)
        val editText = findViewById<EditText>(R.id.layoutEditText)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val intent = Intent(this@SelectRepoActivity, RepoActivity::class.java)


        floatButton.setOnClickListener {
            val text = editText.text
            var listNameRepos = listOf<String>()
            val listRepos: Call<List<Repo>> = retrofitService.listRepos("$text")
            listRepos.enqueue(object : Callback<List<Repo>> {
                override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                    val body = response.body()
                    if (body != null) {
                        for (i in body){
                            listNameRepos+=i.name
                        }
                        intent.putExtra("user", text)
                    }
                    recyclerView.layoutManager = LinearLayoutManager(this@SelectRepoActivity)
                    recyclerView.adapter = RepoAdapter(listNameRepos) {

                        intent.putExtra("title", it)
                        intent.putExtra("user", text.toString())
                        startActivity(intent)

                    }

                }

                override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}

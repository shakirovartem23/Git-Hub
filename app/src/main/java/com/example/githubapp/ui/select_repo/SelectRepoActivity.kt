package com.example.githubapp.ui.select_repo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapp.ui.repo.RepoActivity
import com.example.githubapp.Saved.SaveDataForSelect
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.lang.reflect.Array.get
import kotlin.coroutines.CoroutineContext


open class SelectRepoActivity : AppCompatActivity(){

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(com.example.githubapp.R.layout.select_repo_activity)

        val floatButton = findViewById<FloatingActionButton>(com.example.githubapp.R.id.floatButton)
        val editText = findViewById<EditText>(com.example.githubapp.R.id.layoutEditText)
        val recyclerView: RecyclerView = findViewById(com.example.githubapp.R.id.recyclerView)
        val intent = Intent(this@SelectRepoActivity, RepoActivity::class.java)
        val listResult = SaveDataForSelect()

        floatButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                listResult.loadNameRepos(editText.text.toString())
                recyclerView.layoutManager = LinearLayoutManager(this@SelectRepoActivity)
                recyclerView.adapter = RepoAdapter(listResult.resultNameRepos) {
                    intent.putExtra("title", it)
                    intent.putExtra("user", editText.text.toString())
                    startActivity(intent)
                }
            }
        }
    }
}

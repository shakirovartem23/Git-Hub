package com.example.githubapp.ui.users

import Save_Data.AppDatabase
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
import com.example.githubapp.ui.repo.RepoActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
@RequiresApi(Build.VERSION_CODES.M)
class UsersActivity: AppCompatActivity() {

    val white = ColorStateList.valueOf(resources.getColor(R.color.white))
    val black = ColorStateList.valueOf(resources.getColor(R.color.black))

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

        val starButton = findViewById<MaterialButton>(R.id.star4)
        GlobalScope.launch(Dispatchers.Main) {
            when(employeeDao.selectRepo(repoName).first().favourite){
                true -> starButton.foregroundTintList = white
                else -> starButton.foregroundTintList = black
            }
        }

        title = repoName
        GlobalScope.launch(Dispatchers.Main) {
            val employees = employeeDao.selectStarWO(repoName)

            GlobalScope.launch(Dispatchers.Main) {
                recyclerView.layoutManager = LinearLayoutManager(this@UsersActivity)
                recyclerView.adapter = UsersAdapter(
                    loadUsersOfStarring(userName, repoName, dataValue, employees)
                )
            }
        }

    }
}
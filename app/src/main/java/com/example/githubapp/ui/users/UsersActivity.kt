package com.example.githubapp.ui.users

import Save_Data.AppDatabase
import Save_Data.Repository
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun makerColor(bool: Boolean, starButton: MaterialButton) {
        starButton.foregroundTintList = when(bool){
            true ->
                ColorStateList.valueOf(resources.getColor(R.color.white))
            else ->
                ColorStateList.valueOf(resources.getColor(R.color.black))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users)

        GlobalScope.launch(Dispatchers.Main) {

            findViewById<Toolbar>(R.id.head1).setOnClickListener {
                val intent = Intent(this@UsersActivity, RepoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }


            val dataValue = intent.getStringArrayExtra("DataValue")!!.toList()
            println(dataValue)

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
            val repoName = intent.getStringExtra("title")!!
            val userName = intent.getStringExtra("user")!!

            val employeeDao = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "Star"
            ).build().employeeDao()

            val starButton = findViewById<MaterialButton>(R.id.star4)
            makerColor(
                employeeDao
                    .selectRepo(
                        repoName
                    )
                    .favourite,
                starButton
            )
            starButton.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    var obj = employeeDao.selectRepo(repoName)
                    makerColor(!obj.favourite, starButton)
                    employeeDao
                        .updateRepository(
                            Repository(
                                obj.id,
                                obj.name,
                                obj.ownerName,
                                obj.stargazers_count,
                                !obj.favourite
                            )
                        )
                }
            }

            title = repoName
            val employees = employeeDao.selectStarWO(repoName)

            recyclerView.layoutManager = LinearLayoutManager(this@UsersActivity)
            recyclerView.adapter = UsersAdapter(
                loadUsersOfStarring(userName, repoName, dataValue, employees)
            )
        }

    }
}
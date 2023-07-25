package com.example.githubapp.ui.select_repo

import Save_Data.*
import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.githubapp.MyBroadcastReceiver
import com.example.githubapp.R
import com.example.githubapp.Saved.SaveDataForSelect
import com.example.githubapp.Saved.loadNameRepos
import com.example.githubapp.Saved.returnResult
import com.example.githubapp.data.remove.request_first.Repo
import com.example.githubapp.interactionWithGitHub
import com.example.githubapp.ui.repo.RepoActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)

fun Star.toStar1(star: Star): Star1 {
    val objectOfStar1: Star1
    objectOfStar1 = Star1(
        star.id,
        star.date,
        User1(
            star.userId.id,
            star.userId.name,
            star.userId.avatarUrl
        ),
        Repository1(
            star.repositoryId.id,
            star.repositoryId.name,
            User1(
                star.repositoryId.ownerId.id,
                star.repositoryId.ownerId.name,
                star.repositoryId.ownerId.avatarUrl,
            )
        )
    )
    return objectOfStar1
}

@DelicateCoroutinesApi
class SelectRepoActivity : AppCompatActivity(){

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_repo_activity)

        val am = this.getSystemService(ALARM_SERVICE) as AlarmManager
        val i = Intent(this, MyBroadcastReceiver::class.java)
        val pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    am.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        (1 * 1000).toLong(),
                        pi
                    )
                } else {
                }
            }

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this@SelectRepoActivity,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            -> {
                am.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    (1 * 1000).toLong(),
                    pi
                )
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val floatButton = findViewById<FloatingActionButton>(com.example.githubapp.R.id.floatButton)
        val userName = findViewById<EditText>(R.id.layoutEditText)
        val recyclerView: RecyclerView = findViewById(com.example.githubapp.R.id.recyclerView)

        val employeeDao = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "allStar"
        ).build().employeeDao()

        val callback1 = object: returnResult {
            override fun returnNameRepos(resultNameRepos: List<String>) {
                TODO("Not yet implemented")
            }

            override fun returnTimeStarring(resultTimeStarring: MutableMap<String, String>) {
                TODO("Not yet implemented")
            }

            override fun returnUsersOfStarring(resultUsersOfStarring: MutableList<Triple<String, String, String>>) {
                TODO("Not yet implemented")
            }

            override fun onClick(starButton: MaterialButton, repoName: String) {

            }

        }

        val callback = object: returnResult{
            override fun returnNameRepos(resultNameRepos: List<String>) {
                recyclerView.layoutManager = LinearLayoutManager(this@SelectRepoActivity)
                recyclerView.adapter = SelectRepoAdapter(resources, employeeDao1, callback1, resultNameRepos.toSet().toList()) {
                    val intent = Intent(this@SelectRepoActivity, RepoActivity::class.java)
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

            override fun onClick(starButton: MaterialButton, repoName: String) {
                TODO("Not yet implemented")
            }

        }

        floatButton.setOnClickListener{
            startService(Intent(this@SelectRepoActivity, interactionWithGitHub::class.java).putExtra("userName", userName.text.toString()))

            GlobalScope.launch(Dispatchers.Main) {
                println(employeeDao.allStar())
                if((intent.getStringExtra("userName") ?: "") != ""){
                    loadNameRepos(
                        intent.getStringExtra("userName")!!,
                        employeeDao.selectNameRepos(intent.getStringExtra("userName")!!),
                        callback
                    )
                } else{
                    loadNameRepos(
                        userName.text.toString(),
                        employeeDao.selectNameRepos(userName.text.toString()),
                        callback
                    )
                }
            }
        }
    }
}

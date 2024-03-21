package com.example.githubapp.ui.select_repo

import Save_Data.*
import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.githubapp.LoadingData
import com.example.githubapp.MyBroadcastReceiver
import com.example.githubapp.R
import com.example.githubapp.Saved.loadNameRepos
import com.example.githubapp.ui.repo.RepoActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar

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
                        (8 * 60 * 60 * 1000).toLong(),
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
                    (8 * 60 * 60 * 1000).toLong(),
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
            AppDatabase::class.java, "Star"
        ).build().employeeDao()

        floatButton.setOnClickListener{

            GlobalScope.launch(Dispatchers.Main) {
                println(employeeDao.selectRepos(userName.text.toString()))
                val resultNameRepos = loadNameRepos(
                    userName.text.toString(),
                    employeeDao.selectRepos(userName.text.toString())
                )
                startService(Intent(this@SelectRepoActivity, LoadingData::class.java).putExtra("ownerName", userName.text.toString()))

                recyclerView.layoutManager = LinearLayoutManager(this@SelectRepoActivity)
                recyclerView.adapter = SelectRepoAdapter(resources, employeeDao, resultNameRepos.keys.toList(), userName.text.toString(), resultNameRepos.values.toList()) { repoName ->
                    val intent = Intent(this@SelectRepoActivity, RepoActivity::class.java)
                    intent.putExtra("title", repoName)
                    intent.putExtra("user", userName.text.toString())
                    intent.putExtra("resultNameRepos", resultNameRepos.keys.toTypedArray())
                    startActivity(intent)
                }
            }
        }
    }
}

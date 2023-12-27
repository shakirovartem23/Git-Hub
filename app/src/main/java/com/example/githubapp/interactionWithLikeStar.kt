package com.example.githubapp

import Save_Data.AppDatabase
import Save_Data.Repository
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.githubapp.Saved.SaveDataForSelect
import com.example.githubapp.ui.repo.RepoActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyBroadcastReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotify(
        context: Context,
        countNotify: Int,
        intent: Intent,
        userName: String,
        repoName: String,
        adds: Int
    ){
        val name = "Обновление"
        val descriptionText = "Загрузка завершена!!!"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("notifyDownload", name, importance).apply {
            description = descriptionText
        }
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)

        var text = when(adds>0){
            true -> "Поздравляем $repoName повысилась в рейтинге пользователей на - $adds. Открытие будущего на грани."
            else -> "С $repoName увы сняли звёздочки $adds. Не расстраивайтесь, возможно оно находится ещё в доработке."
        }

        val builder = NotificationCompat.Builder(context!!, "notifyDownload")
            .setSmallIcon(R.drawable.icons8_github_58)
            .setContentTitle("Вести repositories")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent
                .getActivity(context, 0, intent.apply {
                    putExtra("title", repoName)
                    putExtra("user", userName)
                },
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            )

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(countNotify, builder.build())
    }

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {

        val employeeDao = Room.databaseBuilder(
            context!!,
            AppDatabase::class.java, "Star"
        ).build().employeeDao()
        val classResult = SaveDataForSelect()
        GlobalScope.launch(Dispatchers.Main) {
            val repos = employeeDao.allLikeRepos()
            var count = 0

            repos.forEach{
                val result = classResult.loadLikeRepo(it.ownerName, it.name)

                if(result!=null && result.stargazers_count!=it.stargazers_count){
                    count+=1
                    createNotify(context, count, Intent(context, RepoActivity::class.java), it.ownerName, it.name, result.stargazers_count-it.stargazers_count)
                    employeeDao.updateRepository(
                        Repository(
                            it.id,
                            it.name,
                            it.ownerName,
                            result.stargazers_count,
                            it.favourite
                        )
                    )
                }
            }
        }
    }
}

object JustSingleton {
    var countNotify: Int = 0
}
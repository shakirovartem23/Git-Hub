package com.example.githubapp

import Save_Data.AppDatabase1
import Save_Data.Repository1
import Save_Data.Star1
import Save_Data.User1
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
        repoName: String,
        userName: String
    ){
        val name = "Обновление"
        val descriptionText = "Загрузка завершена!!!"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("notifyDownload", name, importance).apply {
            description = descriptionText
        }
        channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)


        val builder = NotificationCompat.Builder(context!!, "notifyDownload")
            .setSmallIcon(R.drawable.icons8_github_58)
            .setContentTitle("Обновление завершено!!!")
            .setContentText(" - $repoName")
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

        val employeeDao1 = Room.databaseBuilder(
            context!!,
            AppDatabase1::class.java, "favouriteStar"
        ).build().employeeDao1()
        val classResult = SaveDataForSelect()
        GlobalScope.launch(Dispatchers.Main) {
            val allLikeStar = employeeDao1
                .allLikeStar()

            allLikeStar.forEach {repo ->
                    classResult.loadUsersOfStarring(
                        repo.repositoryId1.ownerId1.name1,
                        repo.repositoryId1.name1
                    )
                        .forEach { star ->
                            val objectOfStar = Star1(
                                0,
                                star.starred_at,
                                User1(
                                    star.user.id,
                                    star.user.login,
                                    star.user.avatar_url
                                ),
                                Repository1(
                                    repo.repositoryId1.id1,
                                    repo.repositoryId1.name1,
                                    User1(
                                        repo.repositoryId1.ownerId1.id1,
                                        repo.repositoryId1.ownerId1.name1,
                                        repo.repositoryId1.ownerId1.avatarUrl1
                                    )
                                )
                            )
                            if(employeeDao1.allLikeStar().filter { it.repositoryId1.name1==objectOfStar.repositoryId1.name1}.isEmpty()) {
                                JustSingleton.countNotify+=1
                                val intent = Intent(context, RepoActivity::class.java)
                                createNotify(
                                    context,
                                    JustSingleton.countNotify,
                                    intent,
                                    objectOfStar.repositoryId1.name1,
                                    objectOfStar.repositoryId1.ownerId1.name1
                                )
                                employeeDao1.insertOfStar(objectOfStar)
                            }
                        }
                println("ListOfNotify: "+classResult.loadUsersOfStarring(
                    repo.repositoryId1.ownerId1.name1,
                    repo.repositoryId1.name1
                ))
                }
            allLikeStar.forEach {
                employeeDao1.deleteOfStar(it)
            }
            }
    }
}

object JustSingleton {
    var countNotify: Int = 0
}
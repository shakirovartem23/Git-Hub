package com.example.githubapp

import Save_Data.AppDatabase
import Save_Data.Star
import android.app.NotificationChannel
import android.app.NotificationManager
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
        intent: Intent
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
//            .setContentText(" - $repoName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))
            .setAutoCancel(true)
//            .setContentIntent(
////                PendingIntent
////                .getActivity(context, 0, intent.apply {
////                    putExtra("title", repoName)
////                    putExtra("user", userName)
//////                },
////                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//            )

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
            val allLikeStar = employeeDao
                .allStar()


            allLikeStar.forEach {star1 ->
                    classResult.loadUsersOfStarring(
                        star1.userName,
                        star1.repositoryName
                    )
                        .forEach { star ->
                            val objectOfStar = Star(
                                0,
                                star.starred_at,
                                star.user.login,
                                star1.repositoryName
                            )
                            if(employeeDao.allStar().filter { it.repositoryName==objectOfStar.repositoryName}.isEmpty()) {
                                JustSingleton.countNotify+=1
                                val intent = Intent(context, RepoActivity::class.java)
                                createNotify(
                                    context,
                                    JustSingleton.countNotify,
                                    intent
                                )
                                employeeDao.insertStar(objectOfStar)
                            }
                        }
            allLikeStar.forEach {
                employeeDao.deleteStar(it)
            }
            }
        }
    }
}

object JustSingleton {
    var countNotify: Int = 0
}
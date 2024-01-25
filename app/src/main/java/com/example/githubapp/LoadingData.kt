package com.example.githubapp

import Save_Data.AppDatabase
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.room.Room
import com.example.githubapp.Saved.SaveDataForSelect

class LoadingData : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val employeeDao = Room.databaseBuilder(
            this@LoadingData,
            AppDatabase::class.java, "Star"
        ).build().employeeDao()
        val classResult = SaveDataForSelect()

        return super.onStartCommand(intent, flags, startId)
    }
}
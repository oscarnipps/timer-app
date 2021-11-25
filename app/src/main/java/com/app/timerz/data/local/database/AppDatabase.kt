package com.app.timerz.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.timerz.data.local.database.dao.TimerDao
import com.app.timerz.data.local.database.entity.Timer

@Database(
    entities = [
        Timer::class,
    ] ,
    version = 1 ,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

   abstract fun timerDao() : TimerDao
}
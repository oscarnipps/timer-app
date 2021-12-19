package com.app.timerz.data.local.repo

import androidx.lifecycle.LiveData
import com.app.timerz.data.local.database.AppDatabase
import com.app.timerz.data.local.database.dao.TimerDao
import com.app.timerz.data.local.database.entity.Timer
import io.reactivex.Single
import javax.inject.Inject

class TimerRepoImpl @Inject constructor(var timerDao: TimerDao) : TimerRepo {

    override fun createTimer(item: Timer): Single<Long> {
        return timerDao.createTimer(item)
    }

    override fun editTimer(item: Timer): Int{
        return timerDao.editTimer(item)
    }

    override fun getAllTimers(): LiveData<List<Timer>> {
        return timerDao.getAllTimers()
    }

    override fun deleteTimer(id: Int) : Int {
        return timerDao.deleteTimer(id)
    }

    override fun deleteAllTimers() : Int {
        return timerDao.deleteAllTimers()
    }

}
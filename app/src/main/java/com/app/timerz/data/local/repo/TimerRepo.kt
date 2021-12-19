package com.app.timerz.data.local.repo

import androidx.lifecycle.LiveData
import com.app.timerz.data.local.database.entity.Timer
import io.reactivex.Single

interface TimerRepo {

    fun createTimer(item : Timer) : Single<Long>

    fun editTimer(item : Timer) : Int

    fun getAllTimers() : LiveData<List<Timer>>

    fun deleteTimer(id : Int) : Int

    fun deleteAllTimers() : Int
}
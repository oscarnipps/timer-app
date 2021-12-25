package com.app.timerz.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.app.timerz.data.local.database.entity.Timer
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface TimerDao {

    @Insert(onConflict = REPLACE)
    fun createTimer(item: Timer): Single<Long>

    @Update()
    fun editTimer(item: Timer): Int

    @Query("SELECT * FROM Timer")
    fun getAllTimers(): LiveData<List<Timer>>

    @Query("DELETE FROM Timer WHERE id = :id ")
    fun deleteTimer(id: Int): Int

    @Query("DELETE FROM Timer")
    fun deleteAllTimers(): Int
}
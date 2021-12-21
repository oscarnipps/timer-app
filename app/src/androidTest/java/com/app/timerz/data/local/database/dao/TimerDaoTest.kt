package com.app.timerz.data.local.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.timerz.LiveDataTestUtil
import com.app.timerz.data.local.database.AppDatabase
import com.app.timerz.data.local.database.entity.Timer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
class TimerDaoTest {

    @get : Rule
    val hiltRule = HiltAndroidRule(this)

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var timerDao: TimerDao

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase

    @Before
    fun setUp() {
        hiltRule.inject()
        timerDao = database.timerDao()
    }

    @Test
    fun createNewTimer() {
        val timer = Timer(
            1,
            "plank timer",
            "00:01:45",
            ""
        )

        val expectedResult = 1

        val actualResult = timerDao.createTimer(timer).blockingGet()

        assertThat(actualResult).isEqualTo(expectedResult)
    }

    @Test
    fun editTimer() {
        val timer = Timer(
            1,
            "plank timer",
            "00:01:45",
            ""
        )

        timerDao.createTimer(timer).subscribe()

        timer.title = "push up workout"

        timer.timerValue = "00:02:00"

        timer.updatedAt = "2021-12-20 10:30:15"

        timerDao.editTimer(timer)

        val timerList = LiveDataTestUtil.getOrAwaitValue(timerDao.getAllTimers())

        assertThat(timer).isIn(timerList)

        val updatedTimer = timerList?.find { item -> item.id == timer.id }

        assertThat(updatedTimer?.title).isEqualTo(timer.title)

        assertThat(updatedTimer?.timerValue).isEqualTo(timer.timerValue)

        assertThat(updatedTimer?.updatedAt).isEqualTo(timer.updatedAt)
    }

    @After
    fun close() {
        database.close()
    }
}
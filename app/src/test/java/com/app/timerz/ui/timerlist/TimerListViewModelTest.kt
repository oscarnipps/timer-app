package com.app.timerz.ui.timerlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.timerz.data.local.repo.TimerRepo
import com.app.timerz.ui.LiveDataTestUtil
import com.google.common.truth.Truth.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TimerListViewModelTest {

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var timerListViewModel: TimerListViewModel

    @Mock
    private lateinit var timerRepo: TimerRepo

    private lateinit var timerValue: String

    private var timerTitle: String? = null

    @Before
    fun setUp() {
        timerListViewModel = TimerListViewModel(timerRepo)

        timerValue = "00:12:30"

        timerValue = "Workout Timer"
    }

    @Test
    fun `timer value with default selection fails validation`() {
        timerValue = "00:00:00"

        timerListViewModel.validateTimerInput(timerValue,timerTitle)

        val expectedResult = false

        val actualResult = LiveDataTestUtil.getOrAwaitValue(timerListViewModel.inputValidationResult())

        assertTrue(actualResult == expectedResult)
    }

    @Test
    fun `null or empty timer title fails validation`() {
        timerTitle = null

        timerListViewModel.validateTimerInput(timerValue,timerTitle)

        val expectedResult = false

        var actualResult = LiveDataTestUtil.getOrAwaitValue(timerListViewModel.inputValidationResult())

        assertTrue(actualResult == expectedResult)

        timerTitle = ""

        timerListViewModel.validateTimerInput(timerValue,timerTitle)

        actualResult = LiveDataTestUtil.getOrAwaitValue(timerListViewModel.inputValidationResult())

        assertTrue(actualResult == expectedResult)
    }
}
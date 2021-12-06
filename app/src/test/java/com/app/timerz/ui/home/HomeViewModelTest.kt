package com.app.timerz.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.timerz.ui.LiveDataTestUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var homeViewModel : HomeViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
       homeViewModel = HomeViewModel()
    }

    //it is non-empty with non zero values for hr , min and sec
    //it has values for hr , min and sec
    @Test
    fun `empty timer value fails validation`() {
        val timerValue = ""

        homeViewModel.validateTimerValue(timerValue)

        val actualResult = LiveDataTestUtil.getOrAwaitValue(homeViewModel.isTimerValueValid())

        Assert.assertTrue(actualResult == false)

    }

    @Test
    fun `timer value with zero hr, min and second input fails validation`() {
        val timerValue = "00:00:00"

        homeViewModel.validateTimerValue(timerValue)

        val actualResult = LiveDataTestUtil.getOrAwaitValue(homeViewModel.isTimerValueValid())

        Assert.assertTrue(actualResult == false)
    }
}
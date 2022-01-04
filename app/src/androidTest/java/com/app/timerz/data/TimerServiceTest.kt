package com.app.timerz.data

import android.content.Intent
import android.os.IBinder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import com.google.common.truth.Truth
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimerServiceTest {

    @get:Rule
    val serviceRule = ServiceTestRule()


    @Test
    fun service_with_start_action_does_not_have_a_paused_timer() {
        val timerDuration = "00:10:30"

        val timerTitle = "cardio workout"

        val serviceIntent =
            Intent(ApplicationProvider.getApplicationContext(), TimerService::class.java)

        serviceIntent.apply {
            putExtra("timer-value", timerDuration)
            putExtra("timer-title", timerTitle)
            action = Constants.ACTION_START_TIMER
        }

        val binder = serviceRule.bindService(serviceIntent)

        val service = (binder as TimerService.TimerServiceBinder).getTimerService()

        Truth.assertThat(service.isTimerPaused).isFalse()
    }

    @Test
    fun service_with_pause_action_pauses_the_timer() {
        val timerDuration = "00:10:30"

        val timerTitle = "cardio workout"

        val serviceIntent =
            Intent(ApplicationProvider.getApplicationContext(), TimerService::class.java)

        serviceIntent.apply {
            putExtra("timer-value", timerDuration)
            putExtra("timer-title", timerTitle)
            action = Constants.ACTION_START_TIMER
        }

        serviceRule.startService(serviceIntent)

        val binder = serviceRule.bindService(serviceIntent)

        val service = (binder as TimerService.TimerServiceBinder).getTimerService()

        service.pauseTimer()

        Truth.assertThat(service.isTimerPaused).isTrue()
    }

}
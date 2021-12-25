package com.app.timerz.data

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.app.timerz.util.NotificationUtil
import timber.log.Timber
import com.app.timerz.util.TimerUtil


class TimerService : Service() {

    private val binder = TimerServiceBinder()
    private var countDownTimer: CountDownTimer? = null
    private var timerMilliSecondsRemaining: Long? = null
    private var timerTitle: String? = null
    private var timerValue: String? = null
    private lateinit var notification: Notification
    val timerValueLiveData: MutableLiveData<String> = MutableLiveData()
    var isTimerPaused = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand called")

        handleIntentAction(intent)

        return START_STICKY
    }

    private fun handleIntentAction(intent: Intent?) {
        val action = intent?.action

        Timber.d("intent action : $action")

        when (action) {
            Constants.ACTION_PAUSE_TIMER -> pauseTimer()

            Constants.ACTION_RESUME_TIMER -> resumeTimer()

            Constants.ACTION_CANCEL_TIMER -> cancelTimer()

            else -> {
                timerValue = intent?.extras?.getString("timer-value")

                timerTitle = intent?.extras?.getString("timer-title")

                Timber.d("timer value passed to service : $timerValue")

                Timber.d("timer title passed to service : $timerTitle")

                setUpTimer(TimerUtil.getTimerValueInMilliseconds(timerValue!!))
            }

        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private fun setUpTimer(timerDurationMilliSeconds: Long) {
        Timber.d("timer paused : $isTimerPaused")

        Timber.d("countdown timer : $countDownTimer")

        showTimerNotification(timerValue!!, timerTitle!!, Constants.FLAG_RESUME_TIMER)

        if (countDownTimer == null) {

            countDownTimer = getCountDownTimer(timerDurationMilliSeconds)

            if (isTimerPaused) {
                return
            }
        }
        countDownTimer?.start()
    }

    private fun getCountDownTimer(timerDurationMilliSeconds: Long) =
        object : CountDownTimer(timerDurationMilliSeconds, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val formattedValue = TimerUtil.getTimeStringValue(millisUntilFinished)

                timerValueLiveData.value = formattedValue

                timerMilliSecondsRemaining = millisUntilFinished

                Timber.d("onTick : $formattedValue")

                NotificationUtil.updateNotification(
                    this@TimerService,
                    formattedValue,
                    timerTitle!!,
                    Constants.FLAG_PAUSE_TIMER
                )
            }

            override fun onFinish() {
                countDownTimer = null

                isTimerPaused = false

                stopSelf()
            }
        }

    private fun showTimerNotification(timerValue: String, title: String, flag: String) {
        notification = NotificationUtil.getNotification(
            this,
            timerValue,
            title,
            flag
        )

        startForeground(1, notification)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.d("timer service disconnected")
        return super.onUnbind(intent)
    }

    fun cancelTimer() {
        countDownTimer?.cancel()

        countDownTimer = null

        NotificationUtil.cancelNotification(1, this)

        stopSelf()
    }

    fun pauseTimer() {
        countDownTimer?.cancel()

        countDownTimer = null

        NotificationUtil.updateNotification(
            this,
            TimerUtil.getTimeStringValue(timerMilliSecondsRemaining!!),
            timerTitle!!,
            Constants.FLAG_RESUME_TIMER
        )

        isTimerPaused = true
    }

    fun resumeTimer() {
        isTimerPaused = false

        setUpTimer(timerMilliSecondsRemaining!!)
    }

    override fun onDestroy() {
        Timber.d("timer service destroyed")
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Timber.d("onTaskRemoved called")
        super.onTaskRemoved(rootIntent)
    }

    inner class TimerServiceBinder : Binder() {
        fun getTimerService(): TimerService {
            return this@TimerService
        }
    }

}
package com.app.timerz.data

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.app.timerz.util.NotificationUtil
import timber.log.Timber
import com.app.timerz.util.TimerUtil
import android.provider.Settings


class TimerService : Service() {

    private val binder = TimerServiceBinder()
    private var countDownTimer: CountDownTimer? = null
    private var timerMilliSecondsRemaining: Long? = null
    private var timerTitle: String? = null
    private var timerValue: String? = null
    var initialSetTimerValue: String? = null
    val timerValueLiveData: MutableLiveData<String> = MutableLiveData()
    var isTimerPaused = false
    var isTimerFinished = false
    var timerAlertPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand called")

        handleIntent(intent)

        return START_STICKY
    }

    private fun handleIntent(intent: Intent?) {
        val action = intent?.action

        Timber.d("intent action : $action")

        Timber.d("initial set timer value : $initialSetTimerValue")

        when (action) {
            Constants.ACTION_PAUSE_TIMER -> pauseTimer()

            Constants.ACTION_RESUME_TIMER -> resumeTimer()

            Constants.ACTION_CANCEL_TIMER -> cancelTimer()

            Constants.ACTION_START_TIMER -> setTimer(intent)
        }

    }

    private fun setTimer(intent: Intent?) {
        timerTitle = intent?.extras?.getString("timer-title")

        timerValue = intent?.extras?.getString("timer-value")

        Timber.d("timer value : $timerValue")

        Timber.d("timer title : $timerTitle")

        if (initialSetTimerValue == null) {

            initialSetTimerValue = timerValue

            startTimer(TimerUtil.getTimerValueInMilliseconds(timerValue!!))

            return
        }

        if (initialSetTimerValue == Constants.ELAPSED_TIME_VALUE) {
           showFinishedTimer()
            return
        }

        setUpActiveTimer()
    }

    private fun setUpActiveTimer() {
        Timber.d("setting up active timer")

        if (!isTimerPaused) {
            clearTimer()

            startTimer(TimerUtil.getTimerValueInMilliseconds(timerValue))

            return
        }

        timerValueLiveData.value = timerValue
    }

    private fun startTimer(timerDurationMilliSeconds: Long) {
        Timber.d("timer paused : $isTimerPaused")

        Timber.d("countdown timer : $countDownTimer")

        showTimerNotification(timerValue!!, timerTitle!!, Constants.TIMER_RESUMED_STATE)

        countDownTimer = getCountDownTimer(timerDurationMilliSeconds)

        countDownTimer?.start()
    }

    private fun showFinishedTimer() {
        timerValueLiveData.value = initialSetTimerValue
        isTimerFinished = true
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private fun getCountDownTimer(timerDurationMilliSeconds: Long) =
        object : CountDownTimer(timerDurationMilliSeconds, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val formattedValue = TimerUtil.getTimeStringValue(millisUntilFinished)

                timerValueLiveData.value = formattedValue

                timerMilliSecondsRemaining = millisUntilFinished

                NotificationUtil.updateNotification(
                    this@TimerService,
                    formattedValue,
                    timerTitle!!,
                    Constants.TIMER_RESUMED_STATE
                )
            }

            override fun onFinish() {
                Timber.d("onFinish : timer finished")
                countDownTimer = null

                isTimerPaused = false

                playTimerAlertSound()

                timerValueLiveData.value = initialSetTimerValue

                NotificationUtil.updateNotification(
                    this@TimerService,
                    TimerUtil.getTimeStringValue(timerMilliSecondsRemaining!!),
                    timerTitle!!,
                    Constants.TIMER_FINISHED_STATE
                )
            }
        }

    private fun showTimerNotification(timerValue: String, title: String, flag: String) {
        startForeground(1, NotificationUtil.getNotification(
            this,
            timerValue,
            title,
            flag
        ))
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.d("timer service disconnected")
        return super.onUnbind(intent)
    }

    private fun playTimerAlertSound() {
        timerAlertPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)
        timerAlertPlayer?.start()
    }

    fun cancelTimer() {
        clearTimer()

        NotificationUtil.cancelNotification(1, this)

        timerAlertPlayer?.release()

        timerAlertPlayer = null

        stopSelf()

    }

    fun pauseTimer() {
        clearTimer()

        NotificationUtil.updateNotification(
            this,
            TimerUtil.getTimeStringValue(timerMilliSecondsRemaining!!),
            timerTitle!!,
            Constants.TIMER_PAUSED_STATE
        )

        isTimerPaused = true
    }

    private fun clearTimer() {
        countDownTimer?.cancel()

        countDownTimer = null
    }

    fun resumeTimer() {
        Timber.d("timer service resumed")

        isTimerPaused = false

        startTimer(timerMilliSecondsRemaining!!)
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
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
import android.app.NotificationManager
import android.content.Context


class TimerService : Service() {

    private val binder = TimerServiceBinder()
    private val CHANNEL_ID = "channel1"
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var notification: Notification
    val timerValueLiveData: MutableLiveData<String> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        showTimerNotification("00:23:45")
    }

    private fun showTimerNotification(value : String) {
        notification = NotificationUtil.showNotification(this, value)
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        //get the data from the intent (time and flags)
        val value = intent?.extras?.getString("timer-value")

        Timber.d("data passed to service : $value")

        showTimerNotification(value!!)


        countDownTimer = object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                //format to to right string format

                //update livedata
                timerValueLiveData.value = millisUntilFinished.toString()
                Timber.d("onTick : $millisUntilFinished")

                updateNotification(millisUntilFinished.toString())

                showTimerNotification( millisUntilFinished.toString())
            }

            override fun onFinish() {}
        }.start()


        return binder
    }

    private fun updateNotification(value: String) {
        val mNotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, NotificationUtil.showNotification(this, value))
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Timber.d("timer service disconnected")
        return super.onUnbind(intent)

    }

    override fun onDestroy() {
        Timber.d("timer service destroyed")
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }

    inner class TimerServiceBinder : Binder() {
        fun getTimerService(): TimerService {
            return this@TimerService
        }
    }

}
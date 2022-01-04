package com.app.timerz.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.app.timerz.R
import com.app.timerz.data.Constants
import com.app.timerz.data.TimerService
import timber.log.Timber

object NotificationUtil {

    fun getNotification(
        context: Context,
        timeValue: String,
        title: String,
        timerState: String,
    ): Notification {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                "timer-notification-channel",
                "timer service channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.description = "notification for timer service"

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, "timer-notification-channel")
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_add)
            .setContentText(getNotificationContentText(timeValue))
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setOngoing(true)
            .setColorized(true)
            .setContentIntent(getContentPendingIntent(context, timeValue, title))
            .setColor(ContextCompat.getColor(context, R.color.color_button_enabled))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        setUpNotificationActions(notificationBuilder, context, timerState)

        return notificationBuilder.build()
    }

    private fun getNotificationContentText(timeValue: String) : String {
        if (timeValue == "00:00:00") {
            return "Time Elapsed : $timeValue "
        }

        return "Time Remaining : $timeValue"
    }


    private fun setUpNotificationActions(
        notificationBuilder: NotificationCompat.Builder,
        context: Context,
        timerState: String,
    ) {

        when (timerState) {
            Constants.TIMER_PAUSED_STATE -> {
                notificationBuilder.addAction(
                    R.drawable.ic_add,
                    context.getString(R.string.cancel),
                    getCancelPendingIntent(context)
                )

                notificationBuilder.addAction(
                    R.drawable.ic_add,
                    context.getString(R.string.resume),
                    getResumePendingIntent(context)
                )
            }

            Constants.TIMER_RESUMED_STATE -> {
                notificationBuilder.addAction(
                    R.drawable.ic_add,
                    context.getString(R.string.cancel),
                    getCancelPendingIntent(context)
                )

                notificationBuilder.addAction(
                    R.drawable.ic_add,
                    context.getString(R.string.pause),
                    getPausePendingIntent(context)
                )
            }

            Constants.TIMER_FINISHED_STATE -> {
                notificationBuilder.addAction(
                    R.drawable.ic_add,
                    context.getString(R.string.cancel),
                    getCancelPendingIntent(context)
                )
            }
        }

       /* notificationBuilder.addAction(
            R.drawable.ic_add,
            context.getString(R.string.cancel),
            getCancelPendingIntent(context)
        )

        if (timerState == Constants.TIMER_RESUMED_STATE) {
            notificationBuilder.addAction(
                R.drawable.ic_add,
                context.getString(R.string.resume),
                getResumePendingIntent(context)
            )

            return
        }

        notificationBuilder.addAction(
            R.drawable.ic_add,
            context.getString(R.string.pause),
            getPausePendingIntent(context)
        )*/
    }

    fun updateNotification(
        context: Context,
        timeValue: String,
        title: String,
        timerState: String,
    ) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, getNotification(context, timeValue, title, timerState))
    }

    fun cancelNotification(id: Int, context: Context) {
        Timber.d("notification cancelled")
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(id)
    }

    private fun getCancelPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, TimerService::class.java).apply {
            action = Constants.ACTION_CANCEL_TIMER
        }

        return PendingIntent.getService(
            context,
            Constants.CANCEL_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getContentPendingIntent(
        context: Context,
        value: String,
        title: String,
    ): PendingIntent {

        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main_nav_graph)
            .setDestination(R.id.activeTimerFragment)
            .setArguments(
                bundleOf(
                    Pair("timerDuration", value),
                    Pair("timerTitle", title)
                )
            )
            .createPendingIntent()
    }

    private fun getPausePendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, TimerService::class.java).apply {
            action = Constants.ACTION_PAUSE_TIMER
        }

        return PendingIntent.getService(
            context,
            Constants.PAUSE_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getResumePendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, TimerService::class.java).apply {
            action = Constants.ACTION_RESUME_TIMER
        }

        return PendingIntent.getService(
            context,
            Constants.RESUME_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


}
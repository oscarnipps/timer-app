package com.app.timerz.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.CountDownTimer
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.app.timerz.BuildConfig
import com.app.timerz.R
import okhttp3.internal.notify
import java.time.LocalDate

object NotificationUtil {

    fun showNotification(context: Context, contentTitle: String) : Notification {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

       /* val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManagerCompat::class.java
        ) as NotificationManagerCompat*/

        if (SDK_INT >= Build.VERSION_CODES.O) {
            /*val notificationChannelCompat =
                NotificationChannelCompat.Builder("", NotificationManagerCompat.IMPORTANCE_DEFAULT)
                    .setDescription("timer notification channel").build()*/

           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel("timer-notification-channel","timer service channel", NotificationManager.IMPORTANCE_DEFAULT)

                notificationManager.createNotificationChannel(notificationChannel)
            }*/


            val notificationChannel = NotificationChannel("timer-notification-channel","timer service channel", NotificationManager.IMPORTANCE_DEFAULT)

            notificationChannel.description= "notification for timer service"

            notificationManager.createNotificationChannel(notificationChannel)

            //notificationManager.createNotificationChannel(notificationChannelCompat)
        }

        val notificationBuilder = NotificationCompat.Builder(context, "timer-notification-channel")
            .setContentTitle("Workout Timer")
            .setSmallIcon(R.drawable.ic_add)
            .setContentText(contentTitle)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setColorized(true)
            .setColor(ContextCompat.getColor(context, R.color.color_button_enabled))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)



        //notificationManager.notify(1, notificationBuilder.build())
        return notificationBuilder.build()
    }


}
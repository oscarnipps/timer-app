package com.app.timerz.util

import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

object TimerUtil {

    fun getTimeStringValue(millis : Long) : String{

        //convert the time to a readable format
        val formattedValue = String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )

        return formattedValue
    }

    fun getTimerValueInMilliseconds(timeString : String?) : Long {
        val localTime = LocalTime.parse(timeString)
        val milliSeconds = localTime.toSecondOfDay() * 1000
        return milliSeconds.toLong()
    }
}
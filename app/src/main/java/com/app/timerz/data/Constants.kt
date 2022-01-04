package com.app.timerz.data

object Constants {
    const val DATABASE_NAME = "timer.db"
    const val CANCEL_REQUEST_CODE = 10
    const val PAUSE_REQUEST_CODE = 30
    const val RESUME_REQUEST_CODE = 40
    const val ACTION_CANCEL_TIMER = "com.app.timerz.CANCEL_TIMER"
    const val ACTION_FINISHED_TIMER = "com.app.timerz.FINISHED_TIMER"
    const val ACTION_START_TIMER = "com.app.timerz.START_TIMER"
    const val ACTION_PAUSE_TIMER = "com.app.timerz.PAUSE_TIMER"
    const val ACTION_RESUME_TIMER = "com.app.timerz.RESUME_TIMER"
    const val TIMER_RESUMED_STATE = "resumed"
    const val TIMER_FINISHED_STATE = "finished"
    const val TIMER_PAUSED_STATE = "paused"
    const val ELAPSED_TIME_VALUE = "00:00:00"
}
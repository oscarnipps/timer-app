package com.app.timerz.di

import com.app.timerz.data.local.database.AppDatabase
import com.app.timerz.data.local.database.dao.TimerDao
import com.app.timerz.data.local.repo.TimerRepo
import com.app.timerz.data.local.repo.TimerRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TimerModule {

    @Provides
    @Singleton
    fun providesTimerDao (appDatabase: AppDatabase) : TimerDao {
        return appDatabase.timerDao()
    }

    @Provides
    @Singleton
    fun providesTimerRepo (timerRepoImpl: TimerRepoImpl) : TimerRepo {
        return timerRepoImpl
    }
}
package com.app.timerz.di

import android.content.Context
import androidx.room.Room
import com.app.timerz.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class TestDatabaseModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryTestDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.inMemoryDatabaseBuilder(context,AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

}
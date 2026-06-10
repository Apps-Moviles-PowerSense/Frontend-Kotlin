package com.soda.powersense.shared.di

import android.app.Application
import androidx.room.Room
import com.soda.powersense.shared.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesAppDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(
            context = application,
            klass = AppDatabase::class.java,
            name = "PowerSense.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesAlertDao(appDatabase: AppDatabase) = appDatabase.alertDao()

    @Provides
    @Singleton
    fun providesUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    @Singleton
    fun providesDeviceDao(appDatabase: AppDatabase) = appDatabase.deviceDao()

    @Provides
    @Singleton
    fun providesDashboardDao(appDatabase: AppDatabase) = appDatabase.dashboardDao()

    @Provides
    @Singleton
    fun providesScheduleDao(appDatabase: AppDatabase) = appDatabase.scheduleDao()

    @Provides
    @Singleton
    fun providesReportDao(appDatabase: AppDatabase) = appDatabase.reportDao()
}
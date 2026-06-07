package com.soda.powersense.shared.di

import com.soda.powersense.alerts.data.repository.AlertRepositoryImpl
import com.soda.powersense.alerts.domain.repository.AlertRepository
import com.soda.powersense.auth.data.repository.AuthRepositoryImpl
import com.soda.powersense.auth.domain.repository.AuthRepository
import com.soda.powersense.dashboard.data.repository.DashboardRepositoryImpl
import com.soda.powersense.dashboard.domain.repository.DashboardRepository
import com.soda.powersense.devices.data.repository.DeviceRepositoryImpl
import com.soda.powersense.devices.domain.repository.DeviceRepository
import com.soda.powersense.reports.data.repository.ReportRepositoryImpl
import com.soda.powersense.reports.domain.repository.ReportRepository
import com.soda.powersense.schedules.data.repository.ScheduleRepositoryImpl
import com.soda.powersense.schedules.domain.repository.ScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun provideAlertRepository(impl: AlertRepositoryImpl): AlertRepository

    @Binds
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun provideDeviceRepository(impl: DeviceRepositoryImpl): DeviceRepository

    @Binds
    fun provideDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository

    @Binds
    fun provideScheduleRepository(impl: ScheduleRepositoryImpl): ScheduleRepository

    @Binds
    fun provideReportRepository(impl: ReportRepositoryImpl): ReportRepository
}
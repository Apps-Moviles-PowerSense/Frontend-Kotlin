package com.soda.powersense.shared.di

import com.soda.powersense.alerts.data.repository.AlertRepositoryImpl
import com.soda.powersense.alerts.domain.repository.AlertRepository
import com.soda.powersense.auth.data.repository.AuthRepositoryImpl
import com.soda.powersense.auth.domain.repository.AuthRepository
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
}
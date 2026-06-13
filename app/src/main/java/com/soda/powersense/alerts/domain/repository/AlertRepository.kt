package com.soda.powersense.alerts.domain.repository

import com.soda.powersense.alerts.domain.model.Alert
import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    fun getAlerts(): Flow<List<Alert>>

    suspend fun syncAlerts()

    suspend fun acknowledgeAlert(id: String): Result<Unit>

    suspend fun acknowledgeAllAlerts(): Result<Unit>

    suspend fun clearLocalAlerts()
}
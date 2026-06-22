package com.soda.powersense.dashboard.domain.repository

import com.soda.powersense.dashboard.domain.model.DashboardKPIs
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getKPIs(): Flow<DashboardKPIs?>
    suspend fun syncKPIs(): Result<Unit>
}

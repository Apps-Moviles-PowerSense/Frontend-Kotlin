package com.soda.powersense.dashboard.domain.repository

import com.soda.powersense.dashboard.domain.model.DashboardKPIs

interface DashboardRepository {
    suspend fun getKPIs(): Result<DashboardKPIs>
}

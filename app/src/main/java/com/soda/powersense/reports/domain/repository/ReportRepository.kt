package com.soda.powersense.reports.domain.repository

import com.soda.powersense.reports.domain.model.RealtimeConsumption
import com.soda.powersense.reports.domain.model.ReportKPIs
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getKPIs(): Flow<ReportKPIs?>
    fun getRealtimeConsumption(): Flow<List<RealtimeConsumption>>
    suspend fun syncReports(period: String?): Result<Unit>
}

package com.soda.powersense.reports.domain.repository

import com.soda.powersense.reports.domain.model.RealtimeConsumption
import com.soda.powersense.reports.domain.model.ReportKPIs

interface ReportRepository {
    suspend fun getKPIs(): Result<ReportKPIs>
    suspend fun getRealtimeConsumption(period: String?): Result<List<RealtimeConsumption>>
}

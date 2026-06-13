package com.soda.powersense.reports.domain.repository

import com.soda.powersense.reports.domain.model.*
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getKPIs(): Flow<ReportKPIs?>
    fun getMonthlyComparison(): Flow<List<MonthlyComparison>>
    fun getDepartmentMetrics(): Flow<List<DepartmentMetric>>
    fun getReportHistory(): Flow<List<ReportHistory>>
    suspend fun syncReports(
        type: String? = null,
        startDate: String? = null,
        endDate: String? = null
    ): Result<Unit>
}

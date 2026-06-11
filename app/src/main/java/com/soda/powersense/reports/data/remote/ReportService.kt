package com.soda.powersense.reports.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportService {
    @GET("v1/analytics/reports/kpis")
    suspend fun getKPIs(): Response<ReportKPIsDto>

    @GET("v1/analytics/reports/monthly-comparison")
    suspend fun getMonthlyComparison(): Response<List<MonthlyComparisonDto>>

    @GET("v1/analytics/reports/departments")
    suspend fun getDepartmentMetrics(): Response<List<DepartmentMetricDto>>

    @GET("v1/analytics/reports/history")
    suspend fun getReportHistory(): Response<List<ReportHistoryDto>>
}

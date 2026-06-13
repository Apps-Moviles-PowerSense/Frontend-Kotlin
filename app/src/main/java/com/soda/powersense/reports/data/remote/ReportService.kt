package com.soda.powersense.reports.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportService {
    @GET("v1/analytics/reports/kpis")
    suspend fun getKPIs(
        @Query("type") type: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<ReportKPIsDto>

    @GET("v1/analytics/reports/monthly-comparison")
    suspend fun getMonthlyComparison(): Response<List<MonthlyComparisonDto>>

    @GET("v1/analytics/reports/departments")
    suspend fun getDepartmentMetrics(
        @Query("type") type: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<List<DepartmentMetricDto>>

    @GET("v1/analytics/reports/history")
    suspend fun getReportHistory(): Response<List<ReportHistoryDto>>

    @GET("v1/analytics/reports/realtime-consumption")
    suspend fun getRealtimeConsumption(
        @Query("period") period: String? = null
    ): Response<List<RealtimeConsumptionDto>>
}

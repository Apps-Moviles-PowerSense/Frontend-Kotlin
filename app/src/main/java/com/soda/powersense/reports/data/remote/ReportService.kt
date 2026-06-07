package com.soda.powersense.reports.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportService {
    @GET("v1/analytics/reports/kpis")
    suspend fun getKPIs(): Response<ReportKPIsDto>

    @GET("v1/analytics/reports/realtime-consumption")
    suspend fun getRealtimeConsumption(
        @Query("period") period: String? = null
    ): Response<List<RealtimeConsumptionDto>>
}

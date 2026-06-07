package com.soda.powersense.dashboard.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface DashboardService {
    @GET("v1/analytics/dashboard/kpis")
    suspend fun getKPIs(): Response<DashboardKPIsResponse>
}

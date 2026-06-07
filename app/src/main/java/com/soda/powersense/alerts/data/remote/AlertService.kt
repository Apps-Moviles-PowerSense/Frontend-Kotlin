package com.soda.powersense.alerts.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AlertService {

    @GET("v1/analytics/alerts")
    suspend fun getAlerts(
        @Query("type") type: String? = null,
        @Query("severity") severity: String? = null,
        @Query("deviceId") deviceId: String? = null,
        @Query("acknowledged") acknowledged: Boolean? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<List<AlertDto>>

    @GET("v1/analytics/alerts/{id}")
    suspend fun getAlertById(@Path("id") id: String): AlertDto

    @POST("v1/analytics/alerts")
    suspend fun createAlert(
        @Query("type") type: String,
        @Query("severity") severity: String,
        @Query("deviceId") deviceId: String,
        @Query("threshold") threshold: String,
        @Query("message") message: String
    ): AlertDto

    @PATCH("v1/analytics/alerts/{id}/acknowledge")
    suspend fun acknowledgeAlert(@Path("id") id: String): AlertDto

    /*@GET("v1/analytics/alerts/recent")
    suspend fun getRecentAlerts(): Response<?>*/

}
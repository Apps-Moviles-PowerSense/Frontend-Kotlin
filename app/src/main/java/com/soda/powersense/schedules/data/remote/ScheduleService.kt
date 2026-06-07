package com.soda.powersense.schedules.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ScheduleService {
    @GET("v1/inventory/schedules")
    suspend fun getSchedules(): Response<List<ScheduleDto>>

    @PATCH("v1/inventory/schedules/{id}/toggle")
    suspend fun toggleSchedule(
        @Path("id") id: String,
        @Body request: ToggleScheduleRequest
    ): Response<ScheduleDto>
}

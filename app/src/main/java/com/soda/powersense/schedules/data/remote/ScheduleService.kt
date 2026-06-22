package com.soda.powersense.schedules.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ScheduleService {
    @GET("v1/inventory/schedules")
    suspend fun getSchedules(): Response<List<ScheduleDto>>

    @POST("v1/inventory/schedules")
    suspend fun createSchedule(@Body request: CreateScheduleRequest): Response<ScheduleDto>

    @PATCH("v1/inventory/schedules/{id}/toggle")
    suspend fun toggleSchedule(
        @Path("id") id: String,
        @Body request: ToggleScheduleRequest
    ): Response<ScheduleDto>
}

data class CreateScheduleRequest(
    val deviceId: String,
    val deviceName: String,
    val roomName: String,
    val enabled: Boolean,
    val schedules: List<ScheduleEntryRequest>
)

data class ScheduleEntryRequest(
    val action: String,
    val time: TimeSlotRequest,
    val days: List<String>
)

data class TimeSlotRequest(
    val hour: Int,
    val minute: Int
)

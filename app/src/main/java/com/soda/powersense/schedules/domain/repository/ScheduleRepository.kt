package com.soda.powersense.schedules.domain.repository

import com.soda.powersense.schedules.domain.model.Schedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getSchedules(): Flow<List<Schedule>>
    suspend fun syncSchedules(): Result<Unit>
    suspend fun createSchedule(
        deviceId: String,
        deviceName: String,
        roomName: String,
        startTime: String,
        endTime: String,
        days: List<String>
    ): Result<Unit>
    suspend fun toggleSchedule(id: String, enabled: Boolean): Result<Schedule>
}

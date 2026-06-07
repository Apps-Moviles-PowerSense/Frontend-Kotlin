package com.soda.powersense.schedules.domain.repository

import com.soda.powersense.schedules.domain.model.Schedule

interface ScheduleRepository {
    suspend fun getSchedules(): Result<List<Schedule>>
    suspend fun toggleSchedule(id: String, enabled: Boolean): Result<Schedule>
}

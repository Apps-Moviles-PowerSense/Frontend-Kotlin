package com.soda.powersense.schedules.data.repository

import com.soda.powersense.schedules.data.mapper.toDomain
import com.soda.powersense.schedules.data.remote.ScheduleService
import com.soda.powersense.schedules.data.remote.ToggleScheduleRequest
import com.soda.powersense.schedules.domain.model.Schedule
import com.soda.powersense.schedules.domain.repository.ScheduleRepository
import jakarta.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val service: ScheduleService
) : ScheduleRepository {
    override suspend fun getSchedules(): Result<List<Schedule>> {
        return try {
            val response = service.getSchedules()
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleSchedule(id: String, enabled: Boolean): Result<Schedule> {
        return try {
            val response = service.toggleSchedule(id, ToggleScheduleRequest(enabled))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.toDomain())
                } ?: Result.failure(Exception("Null body"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

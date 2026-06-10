package com.soda.powersense.schedules.data.repository

import com.soda.powersense.schedules.data.local.ScheduleDao
import com.soda.powersense.schedules.data.mapper.toDomain
import com.soda.powersense.schedules.data.mapper.toEntity
import com.soda.powersense.schedules.data.remote.ScheduleService
import com.soda.powersense.schedules.data.remote.ToggleScheduleRequest
import com.soda.powersense.schedules.domain.model.Schedule
import com.soda.powersense.schedules.domain.repository.ScheduleRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScheduleRepositoryImpl @Inject constructor(
    private val service: ScheduleService,
    private val dao: ScheduleDao
) : ScheduleRepository {

    override fun getSchedules(): Flow<List<Schedule>> {
        return dao.getSchedules().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncSchedules(): Result<Unit> {
        return try {
            val response = service.getSchedules()
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    dao.upsertAll(dtos.map { it.toEntity() })
                    Result.success(Unit)
                } ?: Result.failure(Exception("Empty response"))
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
                response.body()?.let { dto ->
                    dao.upsert(dto.toEntity())
                    Result.success(dto.toDomain())
                } ?: Result.failure(Exception("Null body"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

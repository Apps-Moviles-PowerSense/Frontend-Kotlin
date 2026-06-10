package com.soda.powersense.dashboard.data.repository

import com.soda.powersense.dashboard.data.local.DashboardDao
import com.soda.powersense.dashboard.data.mapper.toDomain
import com.soda.powersense.dashboard.data.mapper.toEntity
import com.soda.powersense.dashboard.data.remote.DashboardService
import com.soda.powersense.dashboard.domain.model.DashboardKPIs
import com.soda.powersense.dashboard.domain.repository.DashboardRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DashboardRepositoryImpl @Inject constructor(
    private val service: DashboardService,
    private val dao: DashboardDao
) : DashboardRepository {

    override fun getKPIs(): Flow<DashboardKPIs?> {
        return dao.getKPIs().map { it?.toDomain() }
    }

    override suspend fun syncKPIs(): Result<Unit> {
        return try {
            val response = service.getKPIs()
            if (response.isSuccessful) {
                response.body()?.let {
                    dao.upsert(it.toEntity())
                    Result.success(Unit)
                } ?: Result.failure(Exception("Null response"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

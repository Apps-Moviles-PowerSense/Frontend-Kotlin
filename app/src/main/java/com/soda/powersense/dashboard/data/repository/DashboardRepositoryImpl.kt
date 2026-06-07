package com.soda.powersense.dashboard.data.repository

import com.soda.powersense.dashboard.data.mapper.toDomain
import com.soda.powersense.dashboard.data.remote.DashboardService
import com.soda.powersense.dashboard.domain.model.DashboardKPIs
import com.soda.powersense.dashboard.domain.repository.DashboardRepository
import jakarta.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val service: DashboardService
) : DashboardRepository {
    override suspend fun getKPIs(): Result<DashboardKPIs> {
        return try {
            val response = service.getKPIs()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.toDomain())
                } ?: Result.failure(Exception("Null response"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

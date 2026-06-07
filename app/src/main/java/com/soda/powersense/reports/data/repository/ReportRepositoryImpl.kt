package com.soda.powersense.reports.data.repository

import com.soda.powersense.reports.data.mapper.toDomain
import com.soda.powersense.reports.data.remote.ReportService
import com.soda.powersense.reports.domain.model.RealtimeConsumption
import com.soda.powersense.reports.domain.model.ReportKPIs
import com.soda.powersense.reports.domain.repository.ReportRepository
import jakarta.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val service: ReportService
) : ReportRepository {
    override suspend fun getKPIs(): Result<ReportKPIs> {
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

    override suspend fun getRealtimeConsumption(period: String?): Result<List<RealtimeConsumption>> {
        return try {
            val response = service.getRealtimeConsumption(period)
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

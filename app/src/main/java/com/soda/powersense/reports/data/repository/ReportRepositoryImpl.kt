package com.soda.powersense.reports.data.repository

import com.soda.powersense.reports.data.local.ReportDao
import com.soda.powersense.reports.data.mapper.toDomain
import com.soda.powersense.reports.data.mapper.toEntity
import com.soda.powersense.reports.data.remote.ReportService
import com.soda.powersense.reports.domain.model.RealtimeConsumption
import com.soda.powersense.reports.domain.model.ReportKPIs
import com.soda.powersense.reports.domain.repository.ReportRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReportRepositoryImpl @Inject constructor(
    private val service: ReportService,
    private val dao: ReportDao
) : ReportRepository {

    override fun getKPIs(): Flow<ReportKPIs?> {
        return dao.getKPIs().map { it?.toDomain() }
    }

    override fun getRealtimeConsumption(): Flow<List<RealtimeConsumption>> {
        return dao.getHistory().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncReports(period: String?): Result<Unit> {
        return try {
            val kpiResponse = service.getKPIs()
            val historyResponse = service.getRealtimeConsumption(period)

            if (kpiResponse.isSuccessful && historyResponse.isSuccessful) {
                kpiResponse.body()?.let { dao.upsertKPIs(it.toEntity()) }
                historyResponse.body()?.let { dtos ->
                    dao.clearHistory()
                    dao.upsertHistory(dtos.map { it.toEntity() })
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error syncing reports"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

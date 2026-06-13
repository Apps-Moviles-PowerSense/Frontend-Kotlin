package com.soda.powersense.reports.data.repository

import com.soda.powersense.reports.data.local.ReportDao
import com.soda.powersense.reports.data.mapper.toDomain
import com.soda.powersense.reports.data.mapper.toEntity
import com.soda.powersense.reports.data.remote.ReportService
import com.soda.powersense.reports.domain.model.*
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

    override fun getMonthlyComparison(): Flow<List<MonthlyComparison>> {
        return dao.getMonthlyComparison().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getDepartmentMetrics(): Flow<List<DepartmentMetric>> {
        return dao.getDepartmentMetrics().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getReportHistory(): Flow<List<ReportHistory>> {
        return dao.getHistory().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getRealtimeConsumption(period: String): Flow<List<RealtimeConsumption>> {
        return dao.getRealtimeConsumption(period).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncReports(type: String?, startDate: String?, endDate: String?): Result<Unit> {
        return try {
            // Fetch everything, but process individually to be more resilient
            val kpiRes = try { service.getKPIs(type, startDate, endDate) } catch(e: Exception) { null }
            val monthlyRes = try { service.getMonthlyComparison() } catch(e: Exception) { null }
            val deptRes = try { service.getDepartmentMetrics(type, startDate, endDate) } catch(e: Exception) { null }
            val historyRes = try { service.getReportHistory() } catch(e: Exception) { null }
            
            val periodParam = when(type?.lowercase()) {
                "semanal" -> "week"
                "mensual" -> "month"
                else -> "day"
            }
            val realtimeRes = try { service.getRealtimeConsumption(periodParam) } catch(e: Exception) { null }

            kpiRes?.body()?.let { dao.upsertKPIs(it.toEntity()) }
            monthlyRes?.body()?.let {
                dao.clearMonthly()
                dao.upsertMonthlyComparison(it.map { dto -> dto.toEntity() })
            }
            deptRes?.body()?.let {
                dao.clearDepartments()
                dao.upsertDepartmentMetrics(it.map { dto -> dto.toEntity() })
            }
            historyRes?.body()?.let {
                dao.clearHistory()
                dao.upsertHistory(it.map { dto -> dto.toEntity() })
            }
            realtimeRes?.body()?.let { dtos ->
                dao.clearRealtimeConsumption(periodParam)
                dao.upsertRealtimeConsumption(dtos.map { it.toEntity() })
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

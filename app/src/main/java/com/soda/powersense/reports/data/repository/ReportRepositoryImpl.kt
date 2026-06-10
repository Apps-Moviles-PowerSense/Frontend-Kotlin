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

    override suspend fun syncReports(): Result<Unit> {
        return try {
            val kpiRes = service.getKPIs()
            val monthlyRes = service.getMonthlyComparison()
            val deptRes = service.getDepartmentMetrics()
            val historyRes = service.getReportHistory()

            if (kpiRes.isSuccessful && monthlyRes.isSuccessful && deptRes.isSuccessful && historyRes.isSuccessful) {
                kpiRes.body()?.let { dao.upsertKPIs(it.toEntity()) }
                
                monthlyRes.body()?.let {
                    dao.clearMonthly()
                    dao.upsertMonthlyComparison(it.map { dto -> dto.toEntity() })
                }
                
                deptRes.body()?.let {
                    dao.clearDepartments()
                    dao.upsertDepartmentMetrics(it.map { dto -> dto.toEntity() })
                }
                
                historyRes.body()?.let {
                    dao.clearHistory()
                    dao.upsertHistory(it.map { dto -> dto.toEntity() })
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

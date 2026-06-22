package com.soda.powersense.reports.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "report_kpis")
data class ReportKPIsLocalEntity(
    @PrimaryKey
    val id: Int = 1,
    val totalConsumption: Double,
    val totalCost: Double,
    val efficiency: Int,
    val consumptionVariation: Int,
    val costVariation: Int,
    val efficiencyVariation: Int
)

@Entity(tableName = "monthly_comparison")
data class MonthlyComparisonLocalEntity(
    @PrimaryKey
    val month: String,
    val value1: Double,
    val value2: Double
)

@Entity(tableName = "department_metrics")
data class DepartmentMetricLocalEntity(
    @PrimaryKey
    val department: String,
    val current: Double,
    val previous: Double
)

@Entity(tableName = "report_history")
data class ReportHistoryLocalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val period: String,
    val department: String,
    val consumption: Double,
    val cost: Double,
    val variation: Int
)

@Entity(tableName = "consumption_history", primaryKeys = ["label", "period"])
data class ConsumptionLocalEntity(
    val label: String,
    val period: String,
    val consumption: Double
)

@Dao
interface ReportDao {
    @Query("SELECT * FROM report_kpis WHERE id = 1")
    fun getKPIs(): Flow<ReportKPIsLocalEntity?>

    @Upsert
    suspend fun upsertKPIs(kpis: ReportKPIsLocalEntity)

    @Query("SELECT * FROM monthly_comparison")
    fun getMonthlyComparison(): Flow<List<MonthlyComparisonLocalEntity>>

    @Upsert
    suspend fun upsertMonthlyComparison(items: List<MonthlyComparisonLocalEntity>)

    @Query("SELECT * FROM department_metrics")
    fun getDepartmentMetrics(): Flow<List<DepartmentMetricLocalEntity>>

    @Upsert
    suspend fun upsertDepartmentMetrics(items: List<DepartmentMetricLocalEntity>)

    @Query("SELECT * FROM report_history")
    fun getHistory(): Flow<List<ReportHistoryLocalEntity>>

    @Upsert
    suspend fun upsertHistory(items: List<ReportHistoryLocalEntity>)

    @Query("SELECT * FROM consumption_history WHERE period = :period")
    fun getRealtimeConsumption(period: String): Flow<List<ConsumptionLocalEntity>>

    @Upsert
    suspend fun upsertRealtimeConsumption(items: List<ConsumptionLocalEntity>)

    @Query("DELETE FROM monthly_comparison")
    suspend fun clearMonthly()

    @Query("DELETE FROM department_metrics")
    suspend fun clearDepartments()

    @Query("DELETE FROM report_history")
    suspend fun clearHistory()

    @Query("DELETE FROM consumption_history WHERE period = :period")
    suspend fun clearRealtimeConsumption(period: String)
}

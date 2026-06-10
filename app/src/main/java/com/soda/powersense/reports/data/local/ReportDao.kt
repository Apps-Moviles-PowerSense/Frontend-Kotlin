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
    val dailyConsumption: Double,
    val weeklyConsumption: Double,
    val monthlyConsumption: Double,
    val costSaved: Double
)

@Entity(tableName = "consumption_history")
data class ConsumptionLocalEntity(
    @PrimaryKey
    val label: String, // e.g. "01:00", "02:00"
    val period: String,
    val consumption: Double
)

@Dao
interface ReportDao {
    @Query("SELECT * FROM report_kpis WHERE id = 1")
    fun getKPIs(): Flow<ReportKPIsLocalEntity?>

    @Upsert
    suspend fun upsertKPIs(kpis: ReportKPIsLocalEntity)

    @Query("SELECT * FROM consumption_history")
    fun getHistory(): Flow<List<ConsumptionLocalEntity>>

    @Upsert
    suspend fun upsertHistory(items: List<ConsumptionLocalEntity>)

    @Query("DELETE FROM consumption_history")
    suspend fun clearHistory()
}

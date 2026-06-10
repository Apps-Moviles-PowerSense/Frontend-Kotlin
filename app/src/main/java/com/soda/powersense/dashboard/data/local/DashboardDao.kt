package com.soda.powersense.dashboard.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "dashboard_kpis")
data class DashboardKPIsEntity(
    @PrimaryKey
    val id: Int = 1, // Single record
    val totalDevices: Int,
    val activeDevices: Int,
    val totalConsumption: Double,
    val totalCost: Double,
    val efficiency: Double
)

@Dao
interface DashboardDao {
    @Query("SELECT * FROM dashboard_kpis WHERE id = 1")
    fun getKPIs(): Flow<DashboardKPIsEntity?>

    @Upsert
    suspend fun upsert(kpis: DashboardKPIsEntity)

    @Query("DELETE FROM dashboard_kpis")
    suspend fun clear()
}

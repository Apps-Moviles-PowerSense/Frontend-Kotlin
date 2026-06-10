package com.soda.powersense.schedules.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "schedules")
data class ScheduleLocalEntity(
    @PrimaryKey
    val id: String,
    val deviceId: String,
    val deviceName: String,
    val startTime: String,
    val endTime: String,
    val days: String, // Stored as comma-separated string
    val enabled: Boolean
)

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules")
    fun getSchedules(): Flow<List<ScheduleLocalEntity>>

    @Upsert
    suspend fun upsertAll(schedules: List<ScheduleLocalEntity>)

    @Upsert
    suspend fun upsert(schedule: ScheduleLocalEntity)

    @Query("DELETE FROM schedules")
    suspend fun clear()
}

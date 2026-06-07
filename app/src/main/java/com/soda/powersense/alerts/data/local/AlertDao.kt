package com.soda.powersense.alerts.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Query("select * from alerts")
    fun getAlerts(): Flow<List<AlertEntity>>

    @Query("select * from alerts where id = :id")
    suspend fun getAlertById(id: Int): AlertEntity?

    @Upsert
    suspend fun upsert(alertEntity: AlertEntity)

    @Query("update alerts set acknowledged = 1 where id = :id")
    suspend fun acknowledgeAlert(id: String)

    @Query("DELETE FROM alerts")
    suspend fun clearAlerts()
}
package com.soda.powersense.devices.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices")
    fun getDevices(): Flow<List<DeviceEntity>>

    @Upsert
    suspend fun upsertAll(devices: List<DeviceEntity>)

    @Upsert
    suspend fun upsert(device: DeviceEntity)

    @Query("DELETE FROM devices")
    suspend fun clearAll()
}

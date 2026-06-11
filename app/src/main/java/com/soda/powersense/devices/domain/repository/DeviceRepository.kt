package com.soda.powersense.devices.domain.repository

import com.soda.powersense.devices.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getDevices(): Flow<List<Device>>
    suspend fun syncDevices(): Result<Unit>
    suspend fun setDeviceStatus(id: String, status: String): Result<Device>
    suspend fun setAllDevicesStatus(status: String): Result<Unit>
    suspend fun setRoomDevicesStatus(roomId: String, status: String): Result<Unit>
}

package com.soda.powersense.devices.domain.repository

import com.soda.powersense.devices.domain.model.Device

interface DeviceRepository {
    suspend fun getDevices(): Result<List<Device>>
    suspend fun setDeviceStatus(id: String, status: String): Result<Device>
}

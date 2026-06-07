package com.soda.powersense.devices.data.repository

import com.soda.powersense.devices.data.mapper.toDomain
import com.soda.powersense.devices.data.remote.DeviceService
import com.soda.powersense.devices.domain.model.Device
import com.soda.powersense.devices.domain.repository.DeviceRepository
import jakarta.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val service: DeviceService
) : DeviceRepository {

    override suspend fun getDevices(): Result<List<Device>> {
        return try {
            val response = service.getDevices()
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toDomain() } ?: emptyList())
            } else {
                Result.failure(Exception("Error fetching devices: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setDeviceStatus(id: String, status: String): Result<Device> {
        return try {
            val response = service.setDeviceStatus(id, status)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.toDomain())
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                Result.failure(Exception("Error updating device status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

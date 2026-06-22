package com.soda.powersense.devices.data.repository

import com.soda.powersense.devices.data.local.DeviceDao
import com.soda.powersense.devices.data.mapper.toDomain
import com.soda.powersense.devices.data.mapper.toEntity
import com.soda.powersense.devices.data.remote.CreateDeviceRequest
import com.soda.powersense.devices.data.remote.DeviceService
import com.soda.powersense.devices.data.remote.SetRoomStatusRequest
import com.soda.powersense.devices.data.remote.SetStatusRequest
import com.soda.powersense.devices.data.remote.UpdateDeviceRequest
import com.soda.powersense.devices.domain.model.Device
import com.soda.powersense.devices.domain.repository.DeviceRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeviceRepositoryImpl @Inject constructor(
    private val service: DeviceService,
    private val dao: DeviceDao
) : DeviceRepository {

    override fun getDevices(): Flow<List<Device>> {
        return dao.getDevices().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncDevices(): Result<Unit> {
        return try {
            val response = service.getDevices()
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    dao.upsertAll(dtos.map { it.toEntity() })
                    Result.success(Unit)
                } ?: Result.failure(Exception("Empty body"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setDeviceStatus(id: String, status: String): Result<Device> {
        return try {
            val response = service.setDeviceStatus(id, SetStatusRequest(status))
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    dao.upsert(dto.toEntity())
                    Result.success(dto.toDomain())
                } ?: Result.failure(Exception("Null response"))
            } else {
                Result.failure(Exception("Error updating device status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setAllDevicesStatus(status: String): Result<Unit> {
        return try {
            val response = service.setAllDevicesStatus(SetStatusRequest(status))
            if (response.isSuccessful) {
                syncDevices()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setRoomDevicesStatus(roomId: String, status: String): Result<Unit> {
        return try {
            val response = service.setRoomDevicesStatus(roomId, SetRoomStatusRequest(roomId, status))
            if (response.isSuccessful) {
                syncDevices()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createDevice(
        name: String,
        category: String,
        roomId: String,
        roomName: String,
        watts: Int
    ): Result<Device> {
        return try {
            val response = service.createDevice(
                CreateDeviceRequest(name, category, roomId, roomName, watts)
            )
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    dao.upsert(dto.toEntity())
                    Result.success(dto.toDomain())
                } ?: Result.failure(Exception("Null response"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDevice(id: String, roomName: String, watts: Int): Result<Device> {
        return try {
            val response = service.updateDevice(
                id = id,
                request = UpdateDeviceRequest(roomName = roomName, watts = watts)
            )
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    dao.upsert(dto.toEntity())
                    Result.success(dto.toDomain())
                } ?: Result.failure(Exception("Null response"))
            } else {
                Result.failure(Exception("Error updating device: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

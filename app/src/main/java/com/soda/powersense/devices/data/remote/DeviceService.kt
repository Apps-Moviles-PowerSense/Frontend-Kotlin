package com.soda.powersense.devices.data.remote

import retrofit2.Response
import retrofit2.http.*

interface DeviceService {

    @GET("v1/inventory/devices")
    suspend fun getDevices(
        @Query("search") search: String? = null,
        @Query("category") category: String? = null,
        @Query("roomId") roomId: String? = null,
        @Query("status") status: String? = null
    ): Response<List<DeviceDto>>

    @GET("v1/inventory/devices/{id}")
    suspend fun getDeviceById(@Path("id") id: String): Response<DeviceDto>

    @PATCH("v1/inventory/devices/{id}/status")
    suspend fun setDeviceStatus(
        @Path("id") id: String,
        @Body request: SetStatusRequest
    ): Response<DeviceDto>

    @PATCH("v1/inventory/devices/status/all")
    suspend fun setAllDevicesStatus(
        @Body request: SetStatusRequest
    ): Response<Void>

    @PATCH("v1/inventory/rooms/{roomId}/devices/status")
    suspend fun setRoomDevicesStatus(
        @Path("roomId") roomId: String,
        @Body request: SetRoomStatusRequest
    ): Response<Void>

    @POST("v1/inventory/devices")
    suspend fun createDevice(@Body request: CreateDeviceRequest): Response<DeviceDto>

    @PATCH("v1/inventory/devices/{id}")
    suspend fun updateDevice(
        @Path("id") id: String,
        @Body request: UpdateDeviceRequest
    ): Response<DeviceDto>
}

data class CreateDeviceRequest(
    val name: String,
    val category: String,
    val roomId: String,
    val roomName: String,
    val watts: Int
)

data class UpdateDeviceRequest(
    val name: String? = null,
    val category: String? = null,
    val roomId: String? = null,
    val roomName: String? = null,
    val watts: Int? = null
)

data class SetStatusRequest(
    val status: String
)

data class SetRoomStatusRequest(
    val roomId: String,
    val status: String
)

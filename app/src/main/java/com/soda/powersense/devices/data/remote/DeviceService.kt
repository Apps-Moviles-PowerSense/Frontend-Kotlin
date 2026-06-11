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
        @Query("status") status: String
    ): Response<DeviceDto>

    @PATCH("v1/inventory/devices/status/all")
    suspend fun setAllDevicesStatus(
        @Body request: SetStatusRequest
    ): Response<Void>

    @PATCH("v1/inventory/devices/status/room/{roomId}")
    suspend fun setRoomDevicesStatus(
        @Path("roomId") roomId: String,
        @Body request: SetStatusRequest
    ): Response<Void>
}

data class SetStatusRequest(
    val status: String
)

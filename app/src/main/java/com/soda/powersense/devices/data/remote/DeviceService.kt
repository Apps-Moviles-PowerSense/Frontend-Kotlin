package com.soda.powersense.devices.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

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
}

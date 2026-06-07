package com.soda.powersense.devices.data.remote

data class DeviceDto(
    val id: String,
    val name: String,
    val category: String,
    val status: String,
    val location: LocationDto,
    val power: PowerDto
)

data class LocationDto(
    val roomId: String,
    val roomName: String
)

data class PowerDto(
    val watts: Int,
    val voltage: Int?,
    val amperage: Int?
)

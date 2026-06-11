package com.soda.powersense.devices.presentation

import com.soda.powersense.devices.domain.model.Device

data class DeviceState(
    val devices: List<Device> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val totalCount: Int get() = devices.size
    val activeCount: Int get() = devices.count { it.status.lowercase() == "active" }
    val inactiveCount: Int get() = totalCount - activeCount
    val totalConsumptionWatts: Int get() = devices.filter { it.status.lowercase() == "active" }.sumOf { it.watts }
    
    val rooms: List<RoomSummary> get() = devices.groupBy { it.roomId to it.roomName }
        .map { (roomInfo, roomDevices) ->
            RoomSummary(
                id = roomInfo.first,
                name = roomInfo.second,
                activeDevicesCount = roomDevices.count { it.status.lowercase() == "active" },
                totalConsumption = roomDevices.filter { it.status.lowercase() == "active" }.sumOf { it.watts }
            )
        }
}

data class RoomSummary(
    val id: String,
    val name: String,
    val activeDevicesCount: Int,
    val totalConsumption: Int
)

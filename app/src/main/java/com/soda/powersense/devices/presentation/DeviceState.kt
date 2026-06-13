package com.soda.powersense.devices.presentation

import com.soda.powersense.devices.domain.model.Device

data class DeviceState(
    val devices: List<Device> = emptyList(),
    val filteredDevices: List<Device> = emptyList(),
    val searchQuery: String = "",
    val selectedRoom: String? = null,
    val selectedCategory: String? = null,
    val isCreateDialogOpen: Boolean = false,
    val deviceToConfigure: Device? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val totalCount: Int get() = devices.size
    val activeCount: Int get() = devices.count { it.status.lowercase() == "active" }
    val inactiveCount: Int get() = totalCount - activeCount
    val totalConsumptionWatts: Int get() = devices.filter { it.status.lowercase() == "active" }.sumOf { it.watts }
    
    val allRooms: List<String> get() = devices.map { it.roomName }.distinct().sorted()
    val allCategories: List<String> get() = devices.map { it.category }.distinct().sorted()

    val roomsSummary: List<RoomSummary> get() = devices.groupBy { it.roomId to it.roomName }
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

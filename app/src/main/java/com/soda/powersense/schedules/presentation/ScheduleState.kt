package com.soda.powersense.schedules.presentation

import com.soda.powersense.devices.domain.model.Device
import com.soda.powersense.schedules.domain.model.*

data class ScheduleState(
    val schedules: List<Schedule> = emptyList(),
    val filteredSchedules: List<Schedule> = emptyList(),
    val availableDevices: List<Device> = emptyList(),
    val searchQuery: String = "",
    val selectedTab: String = "Dispositivos",
    val selectedRoom: String? = null,
    val rooms: List<String> = emptyList(),
    val stats: ScheduleStats = ScheduleStats("0/0", 0, 0),

    val isCreateDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

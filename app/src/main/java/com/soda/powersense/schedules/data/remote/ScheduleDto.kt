package com.soda.powersense.schedules.data.remote

data class ScheduleDto(
    val id: String,
    val deviceId: String,
    val deviceName: String,
    val roomName: String,
    val enabled: Boolean,
    val schedules: List<ScheduleEntryDto>
)

data class ScheduleEntryDto(
    val action: String,
    val time: String,
    val days: List<String>
)

data class ToggleScheduleRequest(
    val enabled: Boolean
)

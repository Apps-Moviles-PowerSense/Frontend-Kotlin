package com.soda.powersense.schedules.domain.model

data class Schedule(
    val id: String,
    val deviceId: String,
    val deviceName: String,
    val roomName: String,
    val startTime: String,
    val endTime: String,
    val days: List<String>,
    val enabled: Boolean,
    val deviceCategory: String = "GENERIC_POWER"
)

data class ScheduleStats(
    val scheduledDevices: String,
    val activeSchedules: Int,
    val estimatedSavings: Int
)

data class QuickSchedule(
    val id: String,
    val name: String,
    val icon: String
)

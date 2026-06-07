package com.soda.powersense.schedules.domain.model

data class Schedule(
    val id: String,
    val deviceId: String,
    val deviceName: String,
    val startTime: String,
    val endTime: String,
    val days: List<String>,
    val enabled: Boolean
)

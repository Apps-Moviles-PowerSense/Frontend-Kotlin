package com.soda.powersense.schedules.data.mapper

import com.soda.powersense.schedules.data.remote.ScheduleDto
import com.soda.powersense.schedules.domain.model.Schedule

fun ScheduleDto.toDomain(): Schedule {
    val onEntry = schedules.find { it.action == "ON" }
    val offEntry = schedules.find { it.action == "OFF" }
    
    // Use days from ON entry, or OFF entry, or empty list
    val days = onEntry?.days ?: offEntry?.days ?: emptyList()
    
    return Schedule(
        id = id,
        deviceId = deviceId,
        deviceName = deviceName,
        startTime = onEntry?.time ?: "N/A",
        endTime = offEntry?.time ?: "N/A",
        days = days,
        enabled = enabled
    )
}

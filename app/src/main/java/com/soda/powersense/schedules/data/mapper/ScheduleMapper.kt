package com.soda.powersense.schedules.data.mapper

import com.soda.powersense.schedules.data.local.ScheduleLocalEntity
import com.soda.powersense.schedules.data.remote.ScheduleDto
import com.soda.powersense.schedules.domain.model.Schedule

fun ScheduleDto.toDomain(): Schedule {
    val onEntry = schedules.find { it.action == "ON" }
    val offEntry = schedules.find { it.action == "OFF" }
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

fun ScheduleDto.toEntity(): ScheduleLocalEntity {
    val onEntry = schedules.find { it.action == "ON" }
    val offEntry = schedules.find { it.action == "OFF" }
    val days = onEntry?.days ?: offEntry?.days ?: emptyList()

    return ScheduleLocalEntity(
        id = id,
        deviceId = deviceId,
        deviceName = deviceName,
        startTime = onEntry?.time ?: "N/A",
        endTime = offEntry?.time ?: "N/A",
        days = days.joinToString(","),
        enabled = enabled
    )
}

fun ScheduleLocalEntity.toDomain(): Schedule {
    return Schedule(
        id = id,
        deviceId = deviceId,
        deviceName = deviceName,
        startTime = startTime,
        endTime = endTime,
        days = if (days.isEmpty()) emptyList() else days.split(","),
        enabled = enabled
    )
}

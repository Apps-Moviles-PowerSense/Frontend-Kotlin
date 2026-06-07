package com.soda.powersense.alerts.data.mapper

import com.soda.powersense.alerts.data.local.AlertEntity
import com.soda.powersense.alerts.data.remote.AlertDto
import com.soda.powersense.alerts.domain.model.Alert

fun AlertEntity.toDomain(): Alert{
    return Alert(
        id = id,
        type = type,
        severity = severity,
        deviceId = deviceId,
        threshold = threshold,
        message = message,
        acknowledged = acknowledged,
        acknowledgedAt = acknowledgedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun AlertDto.toEntity(): AlertEntity{
    return AlertEntity(
        id = id,
        type = type,
        severity = severity,
        deviceId = deviceId,
        threshold = threshold,
        message = message,
        acknowledged = acknowledged,
        acknowledgedAt = acknowledgedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

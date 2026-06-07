package com.soda.powersense.devices.data.mapper

import com.soda.powersense.devices.data.remote.DeviceDto
import com.soda.powersense.devices.domain.model.Device

fun DeviceDto.toDomain(): Device {
    return Device(
        id = id,
        name = name,
        category = category,
        status = status,
        roomId = location.roomId,
        roomName = location.roomName,
        watts = power.watts
    )
}

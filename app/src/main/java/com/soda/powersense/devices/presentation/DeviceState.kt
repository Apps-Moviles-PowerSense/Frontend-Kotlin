package com.soda.powersense.devices.presentation

import com.soda.powersense.devices.domain.model.Device

data class DeviceState(
    val devices: List<Device> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

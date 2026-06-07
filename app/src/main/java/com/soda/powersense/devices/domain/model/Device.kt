package com.soda.powersense.devices.domain.model

data class Device(
    val id: String,
    val name: String,
    val category: String,
    val status: String,
    val roomId: String,
    val roomName: String,
    val watts: Int
)

package com.soda.powersense.devices.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val category: String,
    val status: String,
    val roomId: String,
    val roomName: String,
    val watts: Int
)

package com.soda.powersense.alerts.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(

    @PrimaryKey
    val id: String,
    val type: String,
    val severity: String,
    val deviceId: String?,
    val threshold: Double?,
    val message: String,
    val acknowledged: Boolean,
    val acknowledgedAt: String?,
    val createdAt: String,
    val updatedAt: String
)

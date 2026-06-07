package com.soda.powersense.alerts.data.remote

data class AlertDto(
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

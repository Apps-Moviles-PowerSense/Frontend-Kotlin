package com.soda.powersense.alerts.domain.model

data class Alert(
    val id: String,
    val type: String,
    val severity: String,
    val deviceId: String,
    val threshold: Double,
    val message: String,
    val acknowledged: Boolean,
    val acknowledgedAt: String,
    val createdAt: String,
    val updatedAt: String
)

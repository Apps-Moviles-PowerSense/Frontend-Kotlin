package com.soda.powersense.dashboard.domain.model

data class DashboardKPIs(
    val totalDevices: Int,
    val activeDevices: Int,
    val totalConsumption: Double,
    val totalCost: Double,
    val efficiency: Double
)

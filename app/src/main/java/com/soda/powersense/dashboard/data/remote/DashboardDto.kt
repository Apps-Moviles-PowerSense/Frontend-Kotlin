package com.soda.powersense.dashboard.data.remote

data class DashboardKPIsResponse(
    val totalDevices: Int,
    val activeDevices: Long,
    val inactiveDevices: Int,
    val totalConsumptionKWh: Double,
    val totalCostUSD: Double,
    val efficiencyPct: Int,
    val estimatedMonthlyCost: Double,
    val monthlySavings: Double,
    val comparison: ComparisonDto
)

data class ComparisonDto(
    val consumptionPct: Int,
    val costPct: Int,
    val efficiencyPct: Int
)

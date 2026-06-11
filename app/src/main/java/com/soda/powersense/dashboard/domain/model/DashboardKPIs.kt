package com.soda.powersense.dashboard.domain.model

data class DashboardKPIs(
    val totalDevices: Int,
    val activeDevices: Int,
    val totalConsumption: Double,
    val totalCost: Double,
    val efficiency: Int,
    val monthlySavings: Double,
    val estimatedCost: Double,
    val consumptionVariation: Int,
    val costVariation: Int,
    val efficiencyVariation: Int
)

data class SavingTip(
    val id: String,
    val title: String,
    val message: String,
    val category: String
)

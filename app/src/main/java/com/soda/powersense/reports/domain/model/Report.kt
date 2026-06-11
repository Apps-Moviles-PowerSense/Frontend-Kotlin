package com.soda.powersense.reports.domain.model

data class ReportKPIs(
    val totalConsumption: Double,
    val totalCost: Double,
    val efficiency: Int,
    val consumptionVariation: Int,
    val costVariation: Int,
    val efficiencyVariation: Int
)

data class MonthlyComparison(
    val month: String,
    val value1: Int, // y2023 or previous
    val value2: Int  // y2024 or current
)

data class DepartmentMetric(
    val department: String,
    val current: Int,
    val previous: Int
)

data class ReportHistory(
    val period: String,
    val department: String,
    val consumption: Int,
    val cost: Int,
    val variation: Int
)

data class RealtimeConsumption(
    val period: String,
    val label: String,
    val consumption: Double
)

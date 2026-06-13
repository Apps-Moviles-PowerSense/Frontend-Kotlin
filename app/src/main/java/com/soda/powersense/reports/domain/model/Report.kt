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
    val value1: Int,
    val value2: Int
)

data class DepartmentMetric(
    val department: String,
    val current: Int,
    val previous: Int
)

data class ReportHistory(
    val id: String = "",
    val period: String,
    val department: String,
    val consumption: Double,
    val cost: Double,
    val variation: Int
)

data class RealtimeConsumption(
    val period: String,
    val label: String,
    val consumption: Double
)

package com.soda.powersense.reports.data.remote

data class ReportKPIsDto(
    val totalConsumptionKWh: Double,
    val totalCostUSD: Double,
    val efficiencyPct: Int,
    val comparison: ComparisonDto
)

data class ComparisonDto(
    val consumptionPct: Int,
    val costPct: Int,
    val efficiencyPct: Int
)

data class MonthlyComparisonDto(
    val month: String,
    val y2023: Int,
    val y2024: Int
)

data class DepartmentMetricDto(
    val department: String,
    val currentPeriod: Int,
    val previousPeriod: Int
)

data class ReportHistoryDto(
    val period: String,
    val department: String,
    val consumption: Int,
    val cost: Int,
    val variation: Int
)

data class RealtimeConsumptionDto(
    val period: String,
    val name: String,
    val value: Double
)

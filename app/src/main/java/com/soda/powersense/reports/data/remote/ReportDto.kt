package com.soda.powersense.reports.data.remote

data class ReportKPIsDto(
    val totalConsumptionKWh: Double,
    val totalCostUSD: Double,
    val efficiencyPct: Int
)

data class RealtimeConsumptionDto(
    val period: String,
    val name: String,
    val value: Double
)

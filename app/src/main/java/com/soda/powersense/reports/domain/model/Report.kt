package com.soda.powersense.reports.domain.model

data class ReportKPIs(
    val dailyConsumption: Double,
    val weeklyConsumption: Double,
    val monthlyConsumption: Double,
    val costSaved: Double
)

data class RealtimeConsumption(
    val period: String,
    val label: String,
    val consumption: Double
)

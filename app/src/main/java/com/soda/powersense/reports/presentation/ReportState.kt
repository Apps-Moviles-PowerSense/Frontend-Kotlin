package com.soda.powersense.reports.presentation

import com.soda.powersense.reports.domain.model.*

data class ReportState(
    val kpis: ReportKPIs? = null,
    val monthlyComparison: List<MonthlyComparison> = emptyList(),
    val departmentMetrics: List<DepartmentMetric> = emptyList(),
    val reportHistory: List<ReportHistory> = emptyList(),
    val reportType: String = "Diario",
    val startDate: String = "01/01/25",
    val endDate: String = "18/09/25",
    val isLoading: Boolean = false,
    val error: String? = null
)

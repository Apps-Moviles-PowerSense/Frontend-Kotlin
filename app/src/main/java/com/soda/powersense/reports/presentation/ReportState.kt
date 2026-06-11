package com.soda.powersense.reports.presentation

import com.soda.powersense.reports.domain.model.*

data class ReportState(
    val kpis: ReportKPIs? = null,
    val monthlyComparison: List<MonthlyComparison> = emptyList(),
    val departmentMetrics: List<DepartmentMetric> = emptyList(),
    val reportHistory: List<ReportHistory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

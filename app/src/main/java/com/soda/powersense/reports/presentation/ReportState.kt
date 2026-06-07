package com.soda.powersense.reports.presentation

import com.soda.powersense.reports.domain.model.RealtimeConsumption
import com.soda.powersense.reports.domain.model.ReportKPIs

data class ReportState(
    val kpis: ReportKPIs? = null,
    val consumptionHistory: List<RealtimeConsumption> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.soda.powersense.dashboard.presentation

import com.soda.powersense.dashboard.domain.model.DashboardKPIs

data class DashboardState(
    val kpis: DashboardKPIs? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

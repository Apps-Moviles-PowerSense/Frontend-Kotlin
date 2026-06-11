package com.soda.powersense.dashboard.presentation

import com.soda.powersense.alerts.domain.model.Alert
import com.soda.powersense.dashboard.domain.model.DashboardKPIs
import com.soda.powersense.devices.domain.model.Device
import com.soda.powersense.reports.domain.model.MonthlyComparison

data class DashboardState(
    val kpis: DashboardKPIs? = null,
    val quickDevices: List<Device> = emptyList(),
    val monthlyComparison: List<MonthlyComparison> = emptyList(),
    val recentAlerts: List<Alert> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

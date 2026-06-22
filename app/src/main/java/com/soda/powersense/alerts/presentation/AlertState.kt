package com.soda.powersense.alerts.presentation

import com.soda.powersense.alerts.domain.model.Alert

data class AlertState(
    val alerts: List<Alert> = emptyList(),
    val filteredAlerts: List<Alert> = emptyList(),
    val selectedType: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

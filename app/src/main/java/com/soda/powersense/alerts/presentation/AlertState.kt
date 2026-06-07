package com.soda.powersense.alerts.presentation

import com.soda.powersense.alerts.domain.model.Alert

data class AlertState(
    val alerts: List<Alert> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.soda.powersense.dashboard.data.mapper

import com.soda.powersense.dashboard.data.remote.DashboardKPIsResponse
import com.soda.powersense.dashboard.domain.model.DashboardKPIs

fun DashboardKPIsResponse.toDomain(): DashboardKPIs {
    return DashboardKPIs(
        totalDevices = totalDevices,
        activeDevices = activeDevices.toInt(),
        totalConsumption = totalConsumptionKWh,
        totalCost = totalCostUSD,
        efficiency = efficiencyPct.toDouble()
    )
}

package com.soda.powersense.reports.data.mapper

import com.soda.powersense.reports.data.remote.RealtimeConsumptionDto
import com.soda.powersense.reports.data.remote.ReportKPIsDto
import com.soda.powersense.reports.domain.model.RealtimeConsumption
import com.soda.powersense.reports.domain.model.ReportKPIs

fun ReportKPIsDto.toDomain(): ReportKPIs {
    return ReportKPIs(
        dailyConsumption = totalConsumptionKWh / 30.0, // Mocked split
        weeklyConsumption = totalConsumptionKWh / 4.0, // Mocked split
        monthlyConsumption = totalConsumptionKWh,
        costSaved = totalCostUSD * 0.1 // Mocked saved
    )
}

fun RealtimeConsumptionDto.toDomain(): RealtimeConsumption {
    return RealtimeConsumption(
        period = period,
        label = name,
        consumption = value
    )
}

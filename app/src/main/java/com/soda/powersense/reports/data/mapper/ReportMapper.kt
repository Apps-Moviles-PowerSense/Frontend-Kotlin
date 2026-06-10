package com.soda.powersense.reports.data.mapper

import com.soda.powersense.reports.data.local.ConsumptionLocalEntity
import com.soda.powersense.reports.data.local.ReportKPIsLocalEntity
import com.soda.powersense.reports.data.remote.RealtimeConsumptionDto
import com.soda.powersense.reports.data.remote.ReportKPIsDto
import com.soda.powersense.reports.domain.model.RealtimeConsumption
import com.soda.powersense.reports.domain.model.ReportKPIs

fun ReportKPIsDto.toDomain(): ReportKPIs {
    return ReportKPIs(
        dailyConsumption = totalConsumptionKWh / 30.0,
        weeklyConsumption = totalConsumptionKWh / 4.0,
        monthlyConsumption = totalConsumptionKWh,
        costSaved = totalCostUSD * 0.1
    )
}

fun ReportKPIsDto.toEntity(): ReportKPIsLocalEntity {
    return ReportKPIsLocalEntity(
        dailyConsumption = totalConsumptionKWh / 30.0,
        weeklyConsumption = totalConsumptionKWh / 4.0,
        monthlyConsumption = totalConsumptionKWh,
        costSaved = totalCostUSD * 0.1
    )
}

fun ReportKPIsLocalEntity.toDomain(): ReportKPIs {
    return ReportKPIs(
        dailyConsumption = dailyConsumption,
        weeklyConsumption = weeklyConsumption,
        monthlyConsumption = monthlyConsumption,
        costSaved = costSaved
    )
}

fun RealtimeConsumptionDto.toDomain(): RealtimeConsumption {
    return RealtimeConsumption(
        period = period,
        label = name,
        consumption = value
    )
}

fun RealtimeConsumptionDto.toEntity(): ConsumptionLocalEntity {
    return ConsumptionLocalEntity(
        period = period,
        label = name,
        consumption = value
    )
}

fun ConsumptionLocalEntity.toDomain(): RealtimeConsumption {
    return RealtimeConsumption(
        period = period,
        label = label,
        consumption = consumption
    )
}

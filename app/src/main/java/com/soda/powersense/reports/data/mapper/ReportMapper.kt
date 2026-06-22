package com.soda.powersense.reports.data.mapper

import com.soda.powersense.reports.data.local.*
import com.soda.powersense.reports.data.remote.*
import com.soda.powersense.reports.domain.model.*

fun ReportKPIsDto.toDomain(): ReportKPIs {
    return ReportKPIs(
        totalConsumption = totalConsumptionKWh,
        totalCost = totalCostUSD,
        efficiency = efficiencyPct,
        consumptionVariation = comparison.consumptionPct,
        costVariation = comparison.costPct,
        efficiencyVariation = comparison.efficiencyPct
    )
}

fun ReportKPIsDto.toEntity(): ReportKPIsLocalEntity {
    return ReportKPIsLocalEntity(
        totalConsumption = totalConsumptionKWh,
        totalCost = totalCostUSD,
        efficiency = efficiencyPct,
        consumptionVariation = comparison.consumptionPct,
        costVariation = comparison.costPct,
        efficiencyVariation = comparison.efficiencyPct
    )
}

fun ReportKPIsLocalEntity.toDomain(): ReportKPIs {
    return ReportKPIs(
        totalConsumption = totalConsumption,
        totalCost = totalCost,
        efficiency = efficiency,
        consumptionVariation = consumptionVariation,
        costVariation = costVariation,
        efficiencyVariation = efficiencyVariation
    )
}

fun MonthlyComparisonDto.toDomain(): MonthlyComparison {
    return MonthlyComparison(month = month, value1 = y2023.toDouble(), value2 = y2024.toDouble())
}

fun MonthlyComparisonDto.toEntity(): MonthlyComparisonLocalEntity {
    return MonthlyComparisonLocalEntity(month = month, value1 = y2023.toDouble(), value2 = y2024.toDouble())
}

fun MonthlyComparisonLocalEntity.toDomain(): MonthlyComparison {
    return MonthlyComparison(month = month, value1 = value1, value2 = value2)
}

fun DepartmentMetricDto.toDomain(): DepartmentMetric {
    return DepartmentMetric(department = departmentName, current = currentPeriod, previous = previousPeriod)
}

fun DepartmentMetricDto.toEntity(): DepartmentMetricLocalEntity {
    return DepartmentMetricLocalEntity(department = departmentName, current = currentPeriod, previous = previousPeriod)
}

fun DepartmentMetricLocalEntity.toDomain(): DepartmentMetric {
    return DepartmentMetric(department = department, current = current, previous = previous)
}

fun ReportHistoryDto.toDomain(): ReportHistory {
    return ReportHistory(period = period, department = department, consumption = consumptionKWh, cost = cost, variation = variationPct)
}

fun ReportHistoryDto.toEntity(): ReportHistoryLocalEntity {
    return ReportHistoryLocalEntity(period = period, department = department, consumption = consumptionKWh, cost = cost, variation = variationPct)
}

fun ReportHistoryLocalEntity.toDomain(): ReportHistory {
    return ReportHistory(period = period, department = department, consumption = consumption, cost = cost, variation = variation)
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

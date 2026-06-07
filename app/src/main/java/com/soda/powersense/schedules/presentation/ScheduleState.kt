package com.soda.powersense.schedules.presentation

import com.soda.powersense.schedules.domain.model.Schedule

data class ScheduleState(
    val schedules: List<Schedule> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.soda.powersense.schedules.presentation

import com.soda.powersense.schedules.domain.model.*

data class ScheduleState(
    val schedules: List<Schedule> = emptyList(),
    val stats: ScheduleStats = ScheduleStats("0/0", 0, 0),
    val quickSchedules: List<QuickSchedule> = listOf(
        QuickSchedule("1", "Toda la casa", "home"),
        QuickSchedule("2", "Solo dormitorios", "bed"),
        QuickSchedule("3", "Areas comunes", "apartment")
    ),
    val isLoading: Boolean = false,
    val error: String? = null
)

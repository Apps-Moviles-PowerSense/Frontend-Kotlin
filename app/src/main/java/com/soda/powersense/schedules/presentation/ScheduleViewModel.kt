package com.soda.powersense.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.schedules.domain.model.Schedule
import com.soda.powersense.schedules.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    val state = _state.asStateFlow()

    init {
        repository.getSchedules()
            .onEach { schedules ->
                _state.update { it.copy(schedules = schedules) }
            }
            .launchIn(viewModelScope)
            
        loadSchedules()
    }

    fun loadSchedules() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.syncSchedules()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun toggleSchedule(schedule: Schedule) {
        viewModelScope.launch {
            repository.toggleSchedule(schedule.id, !schedule.enabled)
                .onFailure { error ->
                    _state.update { it.copy(error = error.message) }
                }
        }
    }
}

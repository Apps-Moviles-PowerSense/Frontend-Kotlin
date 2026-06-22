package com.soda.powersense.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.devices.domain.repository.DeviceRepository
import com.soda.powersense.schedules.domain.model.Schedule
import com.soda.powersense.schedules.domain.model.ScheduleStats
import com.soda.powersense.schedules.domain.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleState())
    val state = _state.asStateFlow()

    init {
        // Observe Schedules
        repository.getSchedules()
            .onEach { schedules ->
                _state.update { currentState ->
                    val rooms = schedules.map { it.roomName }.distinct().sorted()
                    val filtered = applyFilters(schedules, currentState.searchQuery, currentState.selectedRoom)
                    currentState.copy(
                        schedules = schedules,
                        filteredSchedules = filtered,
                        rooms = rooms
                    )
                }
                calculateStats()
            }
            .launchIn(viewModelScope)
            
        loadSchedules()
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        refreshFilters()
    }

    fun onTabSelected(tab: String) {
        _state.update { it.copy(selectedTab = tab) }
        // Currently tabs just visual or could filter categories
    }

    fun onRoomSelected(room: String?) {
        _state.update { it.copy(selectedRoom = room) }
        refreshFilters()
    }

    private fun refreshFilters() {
        val currentState = _state.value
        val filtered = applyFilters(currentState.schedules, currentState.searchQuery, currentState.selectedRoom)
        _state.update { it.copy(filteredSchedules = filtered) }
    }

    private fun applyFilters(schedules: List<Schedule>, query: String, room: String?): List<Schedule> {
        return schedules.filter { schedule ->
            val matchesQuery = schedule.deviceName.contains(query, ignoreCase = true)
            val matchesRoom = room == null || schedule.roomName == room
            matchesQuery && matchesRoom
        }
    }

    private fun calculateStats() {
        viewModelScope.launch {
            val schedules = _state.value.schedules
            val devices = deviceRepository.getDevices().first()
            
            val scheduledDevicesCount = schedules.map { it.deviceId }.distinct().size
            val activeSchedules = schedules.count { it.enabled }
            val estimatedSavings = activeSchedules * 5 // Mock calculation: 5% per active schedule
            
            _state.update { it.copy(
                stats = ScheduleStats(
                    scheduledDevices = "$scheduledDevicesCount/${devices.size}",
                    activeSchedules = activeSchedules,
                    estimatedSavings = estimatedSavings
                )
            ) }
        }
    }

    fun loadSchedules() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.syncSchedules()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    calculateStats()
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

    fun onOpenCreateDialog() {
        viewModelScope.launch {
            val devices = deviceRepository.getDevices().first()
            _state.update { it.copy(availableDevices = devices, isCreateDialogOpen = true) }
        }
    }

    fun onCloseCreateDialog() {
        _state.update { it.copy(isCreateDialogOpen = false) }
    }

    fun createSchedule(deviceId: String, startTime: String, endTime: String, days: List<String>) {
        val device = _state.value.availableDevices.find { it.id == deviceId } ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.createSchedule(
                deviceId = device.id,
                deviceName = device.name,
                roomName = device.roomName,
                startTime = startTime,
                endTime = endTime,
                days = days
            ).onSuccess {
                onCloseCreateDialog()
                loadSchedules()
            }.onFailure { error ->
                _state.update { it.copy(error = error.message, isLoading = false) }
            }
        }
    }
}

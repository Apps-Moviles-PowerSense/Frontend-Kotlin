package com.soda.powersense.devices.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.devices.domain.model.Device
import com.soda.powersense.devices.domain.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceState())
    val state = _state.asStateFlow()

    init {
        repository.getDevices()
            .onEach { devices ->
                _state.update { currentState ->
                    val filtered = applyFilters(devices, currentState.searchQuery, currentState.selectedRoom, currentState.selectedCategory)
                    currentState.copy(devices = devices, filteredDevices = filtered)
                }
            }
            .launchIn(viewModelScope)
            
        loadDevices()
    }

    fun loadDevices() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.syncDevices()
                .onFailure { error ->
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
                .onSuccess {
                    _state.update { it.copy(isLoading = false, error = null) }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        refreshFilters()
    }

    fun onRoomSelected(room: String?) {
        _state.update { it.copy(selectedRoom = room) }
        refreshFilters()
    }

    fun onCategorySelected(category: String?) {
        _state.update { it.copy(selectedCategory = category) }
        refreshFilters()
    }

    private fun refreshFilters() {
        val currentState = _state.value
        val filtered = applyFilters(currentState.devices, currentState.searchQuery, currentState.selectedRoom, currentState.selectedCategory)
        _state.update { it.copy(filteredDevices = filtered) }
    }

    private fun applyFilters(devices: List<Device>, query: String, room: String?, category: String?): List<Device> {
        return devices.filter { device ->
            val matchesQuery = device.name.contains(query, ignoreCase = true)
            val matchesRoom = room == null || device.roomName == room
            val matchesCategory = category == null || device.category == category
            matchesQuery && matchesRoom && matchesCategory
        }
    }

    fun setAllDevicesStatus(active: Boolean) {
        val status = if (active) "active" else "inactive"
        viewModelScope.launch {
            repository.setAllDevicesStatus(status)
        }
    }

    fun setRoomDevicesStatus(roomId: String, active: Boolean) {
        val status = if (active) "active" else "inactive"
        viewModelScope.launch {
            repository.setRoomDevicesStatus(roomId, status)
        }
    }

    fun toggleDevice(device: Device) {
        val nextStatus = if (device.status.lowercase() == "active") "inactive" else "active"
        viewModelScope.launch {
            repository.setDeviceStatus(device.id, nextStatus)
                .onFailure { error ->
                    _state.update { it.copy(error = error.message) }
                }
        }
    }

    fun onOpenCreateDialog() {
        _state.update { it.copy(isCreateDialogOpen = true) }
    }

    fun onCloseCreateDialog() {
        _state.update { it.copy(isCreateDialogOpen = false) }
    }

    fun onOpenConfigureDialog(device: Device) {
        _state.update { it.copy(deviceToConfigure = device) }
    }

    fun onCloseConfigureDialog() {
        _state.update { it.copy(deviceToConfigure = null) }
    }

    fun createDevice(name: String, category: String, roomId: String, roomName: String, watts: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.createDevice(name, category, roomId, roomName, watts)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isCreateDialogOpen = false) }
                    loadDevices()
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun updateDevice(id: String, roomName: String, watts: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.updateDevice(id, roomName, watts)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, deviceToConfigure = null) }
                    loadDevices()
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }
}

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
                _state.update { it.copy(devices = devices) }
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

    fun toggleDevice(device: Device) {
        val nextStatus = if (device.status.lowercase() == "active") "inactive" else "active"
        viewModelScope.launch {
            repository.setDeviceStatus(device.id, nextStatus)
                .onFailure { error ->
                    _state.update { it.copy(error = error.message) }
                }
        }
    }
}

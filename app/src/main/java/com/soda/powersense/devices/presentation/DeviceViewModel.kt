package com.soda.powersense.devices.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.devices.domain.model.Device
import com.soda.powersense.devices.domain.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceState())
    val state = _state.asStateFlow()

    init {
        loadDevices()
    }

    fun loadDevices() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getDevices()
                .onSuccess { devices ->
                    _state.update { it.copy(devices = devices, isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun toggleDevice(device: Device) {
        val nextStatus = if (device.status.lowercase() == "active") "inactive" else "active"
        viewModelScope.launch {
            repository.setDeviceStatus(device.id, nextStatus)
                .onSuccess { updated ->
                    _state.update { currentState ->
                        currentState.copy(
                            devices = currentState.devices.map { d ->
                                if (d.id == updated.id) updated else d
                            }
                        )
                    }
                }
        }
    }
}

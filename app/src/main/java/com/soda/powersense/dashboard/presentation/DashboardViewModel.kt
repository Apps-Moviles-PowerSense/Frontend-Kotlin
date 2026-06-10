package com.soda.powersense.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.alerts.domain.repository.AlertRepository
import com.soda.powersense.dashboard.domain.repository.DashboardRepository
import com.soda.powersense.devices.domain.model.Device
import com.soda.powersense.devices.domain.repository.DeviceRepository
import com.soda.powersense.reports.domain.model.MonthlyComparison
import com.soda.powersense.reports.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    private val deviceRepository: DeviceRepository,
    private val reportRepository: ReportRepository,
    private val alertRepository: AlertRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        // Observe KPIs
        repository.getKPIs()
            .onEach { kpis -> _state.update { it.copy(kpis = kpis) } }
            .launchIn(viewModelScope)

        // Observe Devices for Quick Control
        deviceRepository.getDevices()
            .onEach { devices -> _state.update { it.copy(quickDevices = devices.take(3)) } }
            .launchIn(viewModelScope)

        // Observe Monthly Comparison for Chart
        reportRepository.getMonthlyComparison()
            .onEach { history -> _state.update { it.copy(monthlyComparison = history) } }
            .launchIn(viewModelScope)

        // Observe Recent Alerts
        alertRepository.getAlerts()
            .onEach { alerts -> _state.update { it.copy(recentAlerts = alerts.take(3)) } }
            .launchIn(viewModelScope)
            
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.syncKPIs()
            deviceRepository.syncDevices()
            reportRepository.syncReports()
            kotlin.runCatching { alertRepository.syncAlerts() }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun toggleDevice(device: Device) {
        val nextStatus = if (device.status.lowercase() == "active") "inactive" else "active"
        viewModelScope.launch {
            deviceRepository.setDeviceStatus(device.id, nextStatus)
        }
    }
}

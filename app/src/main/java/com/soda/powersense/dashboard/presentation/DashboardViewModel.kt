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
import kotlinx.coroutines.Job
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

    private var chartJob: Job? = null

    init {
        // Observe KPIs
        repository.getKPIs()
            .onEach { kpis -> _state.update { it.copy(kpis = kpis) } }
            .launchIn(viewModelScope)

        // Observe Devices for Quick Control (Top 3 by consumption)
        deviceRepository.getDevices()
            .onEach { devices -> 
                val top3 = devices.sortedByDescending { it.watts }.take(3)
                _state.update { it.copy(quickDevices = top3) } 
            }
            .launchIn(viewModelScope)

        // Observe Recent Alerts
        alertRepository.getAlerts()
            .onEach { alerts -> 
                val recent = alerts.filter { !it.acknowledged }.take(3)
                _state.update { it.copy(recentAlerts = recent) } 
            }
            .launchIn(viewModelScope)
            
        observeChartData("day")
        loadData()
    }

    fun onPeriodChange(period: String) {
        val type = when(period) {
            "Diario" -> "day"
            "Semanal" -> "week"
            "Mensual" -> "month"
            else -> "day"
        }
        _state.update { it.copy(selectedPeriod = period) }
        observeChartData(type)
        
        viewModelScope.launch {
            reportRepository.syncReports(type = period)
        }
    }

    private fun observeChartData(period: String) {
        chartJob?.cancel()
        chartJob = reportRepository.getRealtimeConsumption(period)
            .onEach { history ->
                // Map RealtimeConsumption to MonthlyComparison for the existing chart UI
                val chartItems = history.map { 
                    MonthlyComparison(
                        month = it.label,
                        value1 = it.consumption,
                        value2 = it.consumption * 0.9 // Fake comparison
                    )
                }
                _state.update { it.copy(monthlyComparison = chartItems) }
            }
            .launchIn(viewModelScope)
    }

    fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.syncKPIs()
            deviceRepository.syncDevices()
            reportRepository.syncReports(type = _state.value.selectedPeriod)
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

    fun acknowledgeAlert(id: String) {
        viewModelScope.launch {
            alertRepository.acknowledgeAlert(id)
        }
    }
}

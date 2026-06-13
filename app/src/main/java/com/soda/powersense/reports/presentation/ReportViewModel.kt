package com.soda.powersense.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.reports.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReportState())
    val state = _state.asStateFlow()

    init {
        repository.getKPIs()
            .onEach { kpis -> _state.update { it.copy(kpis = kpis) } }
            .launchIn(viewModelScope)

        repository.getMonthlyComparison()
            .onEach { items -> _state.update { it.copy(monthlyComparison = items) } }
            .launchIn(viewModelScope)

        repository.getDepartmentMetrics()
            .onEach { items -> _state.update { it.copy(departmentMetrics = items) } }
            .launchIn(viewModelScope)

        repository.getReportHistory()
            .onEach { items -> _state.update { it.copy(reportHistory = items) } }
            .launchIn(viewModelScope)

        loadData()
    }

    fun onReportTypeChange(type: String) {
        _state.update { it.copy(reportType = type) }
    }

    fun onStartDateChange(date: String) {
        _state.update { it.copy(startDate = date) }
    }

    fun onEndDateChange(date: String) {
        _state.update { it.copy(endDate = date) }
    }

    fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.syncReports(
                type = _state.value.reportType,
                startDate = _state.value.startDate,
                endDate = _state.value.endDate
            ).onFailure { error ->
                _state.update { it.copy(error = error.message, isLoading = false) }
            }.onSuccess {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

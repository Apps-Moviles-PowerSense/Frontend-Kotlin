package com.soda.powersense.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.reports.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: ReportRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReportState())
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val kpiResult = repository.getKPIs()
            val consumptionResult = repository.getRealtimeConsumption("day")

            if (kpiResult.isSuccess && consumptionResult.isSuccess) {
                _state.update { it.copy(
                    kpis = kpiResult.getOrNull(),
                    consumptionHistory = consumptionResult.getOrDefault(emptyList()),
                    isLoading = false
                ) }
            } else {
                _state.update { it.copy(
                    error = "Failed to load report data",
                    isLoading = false
                ) }
            }
        }
    }
}

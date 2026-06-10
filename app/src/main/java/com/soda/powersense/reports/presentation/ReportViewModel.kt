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

        repository.getRealtimeConsumption()
            .onEach { history -> _state.update { it.copy(consumptionHistory = history) } }
            .launchIn(viewModelScope)

        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.syncReports("day")
                .onFailure { error ->
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}

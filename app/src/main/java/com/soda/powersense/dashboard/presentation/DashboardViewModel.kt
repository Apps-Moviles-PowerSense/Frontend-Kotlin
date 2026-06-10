package com.soda.powersense.dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.dashboard.domain.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        repository.getKPIs()
            .onEach { kpis ->
                _state.update { it.copy(kpis = kpis) }
            }
            .launchIn(viewModelScope)
            
        loadKPIs()
    }

    fun loadKPIs() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.syncKPIs()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }
}

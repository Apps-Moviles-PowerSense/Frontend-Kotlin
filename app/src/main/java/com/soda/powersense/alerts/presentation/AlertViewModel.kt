package com.soda.powersense.alerts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.alerts.domain.repository.AlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AlertViewModel @Inject constructor(
    private val repository: AlertRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AlertState())
    val state: StateFlow<AlertState> = _state

    init {
        observeAlerts()
        syncAlerts()
    }

    private fun observeAlerts(){
        viewModelScope.launch {
            repository.getAlerts().collect { alerts ->
                _state.update {
                    it.copy(alerts = alerts)
                }
            }
        }
    }

    private fun syncAlerts() {
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            try {
                repository.syncAlerts()
                _state.update {
                    it.copy(isLoading = false)
                }
            }catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Synchronization failed"
                    )
                }
            }
        }
    }

    fun acknowledgeAlert(id: String){
        viewModelScope.launch {
            repository.acknowledgeAlert(id)
        }
    }

}
package com.soda.powersense.auth.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soda.powersense.auth.domain.model.AuthResult
import com.soda.powersense.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val session: AuthResult) : ProfileState()
    object Unauthenticated : ProfileState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val state: StateFlow<ProfileState> = repository.getSession()
        .map { session ->
            if (session != null) ProfileState.Success(session)
            else ProfileState.Unauthenticated
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileState.Loading
        )

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            repository.updateName(newName)
        }
    }
}

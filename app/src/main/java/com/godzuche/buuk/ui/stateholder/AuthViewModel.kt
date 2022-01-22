package com.godzuche.buuk.ui.stateholder

import androidx.lifecycle.ViewModel
import com.godzuche.buuk.ui.state.AuthUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel: ViewModel() {
    // Expose the ui state in an observable data holder like LiveData and StateFlow
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
}
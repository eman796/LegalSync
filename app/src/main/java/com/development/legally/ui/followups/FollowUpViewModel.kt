package com.development.legally.ui.followups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.FollowUp
import com.development.legally.data.repository.FollowUpRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FollowUpsUiState(
    val followUps: List<FollowUp> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class FollowUpViewModel : ViewModel() {

    private val repository = FollowUpRepository()

    private val _uiState = MutableStateFlow(FollowUpsUiState())
    val uiState: StateFlow<FollowUpsUiState> = _uiState

    fun loadFollowUps(caseId: String) {
        _uiState.value = _uiState.value.copy(loading = true)
        viewModelScope.launch {
            val res = repository.getFollowUpsByCase(caseId)
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.value = FollowUpsUiState(followUps = list)
            } else {
                _uiState.value = _uiState.value.copy(error = res.exceptionOrNull()?.message)
            }
        }
    }

    fun createFollowUp(followUp: FollowUp, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.createFollowUp(followUp)
            if (res.isSuccess) onDone(true, null) else onDone(false, res.exceptionOrNull()?.message)
        }
    }
}

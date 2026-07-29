package com.development.legally.ui.followups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.FollowUp
import com.development.legally.data.repository.FollowUpRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FollowUpsUiState(
    val followups: List<FollowUp> = emptyList(),
    val filtered: List<FollowUp> = emptyList(),
    val selectedFollowUp: FollowUp? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

class FollowUpViewModel : ViewModel() {

    private val repository = FollowUpRepository()

    private val _uiState = MutableStateFlow(FollowUpsUiState())
    val uiState: StateFlow<FollowUpsUiState> = _uiState

    fun loadFollowUpsByCase(caseId: String) {
        _uiState.value = _uiState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            val res = repository.getFollowUpsByCase(caseId)
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.value = FollowUpsUiState(
                    followups = list,
                    filtered = list,
                    loading = false,
                    searchQuery = _uiState.value.searchQuery
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    error = res.exceptionOrNull()?.message,
                    loading = false
                )
            }
        }
    }

    fun searchFollowUps(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        val filtered = if (query.isBlank()) {
            _uiState.value.followups
        } else {
            _uiState.value.followups.filter { f ->
                val description = f.description.lowercase()
                val responsible = f.responsibleUser.lowercase()
                val date = f.date.lowercase()
                val queryLower = query.lowercase()
                
                description.contains(queryLower) ||
                responsible.contains(queryLower) ||
                date.contains(queryLower)
            }
        }
        _uiState.value = _uiState.value.copy(filtered = filtered)
    }

    fun createFollowUp(followUp: FollowUp, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.createFollowUp(followUp)
            if (res.isSuccess) {
                onDone(true, null)
            } else {
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun updateFollowUp(followUp: FollowUp, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.updateFollowUp(followUp)
            if (res.isSuccess) {
                onDone(true, null)
            } else {
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun deleteFollowUp(followUpId: String, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.deleteFollowUp(followUpId)
            if (res.isSuccess) {
                onDone(true, null)
            } else {
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSelectedFollowUp() {
        _uiState.value = _uiState.value.copy(selectedFollowUp = null)
    }
}

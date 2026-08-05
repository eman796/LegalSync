package com.development.legally.ui.cases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.Case
import com.development.legally.data.repository.CaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CasosUiState(
    val cases: List<Case> = emptyList(),
    val filtered: List<Case> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filterStatus: String = "Todos",
    val filterPriority: String = "Todas"
)

class CasosViewModel : ViewModel() {
    private val repository = CaseRepository()
    private val _uiState = MutableStateFlow(CasosUiState())
    val uiState: StateFlow<CasosUiState> = _uiState

    init {
        loadCasos()
    }

    fun loadCasos() {
        _uiState.value = _uiState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            val res = repository.getCases()
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.value = _uiState.value.copy(
                    cases = list,
                    loading = false
                )
                applyFilters()
            } else {
                _uiState.value = _uiState.value.copy(
                    error = res.exceptionOrNull()?.message,
                    loading = false
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun updateStatusFilter(status: String) {
        _uiState.value = _uiState.value.copy(filterStatus = status)
        applyFilters()
    }

    fun updatePriorityFilter(priority: String) {
        _uiState.value = _uiState.value.copy(filterPriority = priority)
        applyFilters()
    }

    private fun applyFilters() {
        val currentState = _uiState.value
        val query = currentState.searchQuery.lowercase()
        val status = currentState.filterStatus
        val priority = currentState.filterPriority

        val filteredList = currentState.cases.filter { c ->
            val matchesQuery = if (query.isBlank()) true else {
                c.caseNumber.lowercase().contains(query) ||
                        c.description.lowercase().contains(query) ||
                        c.clientName.lowercase().contains(query)
            }

            val matchesStatus = if (status == "Todos") true else c.status == status
            val matchesPriority = if (priority == "Todas") true else c.priority == priority

            matchesQuery && matchesStatus && matchesPriority
        }

        _uiState.value = _uiState.value.copy(filtered = filteredList)
    }
}
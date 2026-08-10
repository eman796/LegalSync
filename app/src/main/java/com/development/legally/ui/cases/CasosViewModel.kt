package com.development.legally.ui.cases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.Case
import com.development.legally.data.repository.CaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CasosUiState(
    val cases: List<Case> = emptyList(),
    val filtered: List<Case> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filterStatus: String = "Todos",
    val filterPriority: String = "Todas",
    
    // Campos para edición
    val currentCaseId: String? = null,
    val numeroExpediente: String = "",
    val tituloCaso: String = "",
    val tipoProceso: String = "",
    val estadoCaso: String = "",
    val descripcion: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class CasosViewModel : ViewModel() {
    private val repository = CaseRepository()
    private val _uiState = MutableStateFlow(CasosUiState())
    val uiState: StateFlow<CasosUiState> = _uiState.asStateFlow()

    fun loadCasos() {
        if (_uiState.value.loading) return
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            val res = repository.getCases()
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.update { state ->
                    state.copy(
                        cases = list,
                        loading = false,
                        filtered = applyFilterLogic(list, state.searchQuery, state.filterStatus, state.filterPriority)
                    )
                }
            } else {
                _uiState.update { it.copy(
                    error = res.exceptionOrNull()?.message,
                    loading = false
                ) }
            }
        }
    }

    fun setCaseForEditing(caseId: String?) {
        if (caseId == null) {
            resetForm()
            return
        }
        val case = _uiState.value.cases.find { it.firestoreDocId == caseId }
        if (case != null) {
            populateForm(case)
        } else {
            viewModelScope.launch {
                val res = repository.getCaseById(caseId)
                res.getOrNull()?.let { populateForm(it) }
            }
        }
    }

    private fun populateForm(case: Case) {
        _uiState.update { it.copy(
            currentCaseId = case.firestoreDocId,
            numeroExpediente = case.caseNumber,
            tituloCaso = case.CaseTittle,
            tipoProceso = case.processType,
            estadoCaso = case.status,
            descripcion = case.description
        ) }
    }

    private fun resetForm() {
        _uiState.update { it.copy(
            currentCaseId = null,
            numeroExpediente = "", tituloCaso = "", tipoProceso = "", estadoCaso = "", descripcion = ""
        ) }
    }

    fun onNumeroExpedienteChange(v: String) { _uiState.update { it.copy(numeroExpediente = v) } }
    fun onTituloCasoChange(v: String) { _uiState.update { it.copy(tituloCaso = v) } }
    fun onTipoProcesoChange(v: String) { _uiState.update { it.copy(tipoProceso = v) } }
    fun onEstadoCasoChange(v: String) { _uiState.update { it.copy(estadoCaso = v) } }
    fun onDescripcionChange(v: String) { _uiState.update { it.copy(descripcion = v) } }

    fun updateSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filtered = applyFilterLogic(state.cases, query, state.filterStatus, state.filterPriority)
            )
        }
    }

    fun updateStatusFilter(status: String) {
        _uiState.update { state ->
            state.copy(
                filterStatus = status,
                filtered = applyFilterLogic(state.cases, state.searchQuery, status, state.filterPriority)
            )
        }
    }

    fun updatePriorityFilter(priority: String) {
        _uiState.update { state ->
            state.copy(
                filterPriority = priority,
                filtered = applyFilterLogic(state.cases, state.searchQuery, state.filterStatus, priority)
            )
        }
    }

    private fun applyFilterLogic(cases: List<Case>, query: String, status: String, priority: String): List<Case> {
        val q = query.lowercase()
        return cases.filter { c ->
            val matchesQuery = if (q.isBlank()) true else {
                c.caseNumber.lowercase().contains(q) ||
                        c.description.lowercase().contains(q) ||
                        c.clientName.lowercase().contains(q)
            }
            val matchesStatus = if (status == "Todos") true else c.status == status
            val matchesPriority = if (priority == "Todas") true else c.priority == priority
            matchesQuery && matchesStatus && matchesPriority
        }
    }

    fun guardarCaso() {
        val state = _uiState.value
        val caseId = state.currentCaseId ?: return
        
        _uiState.update { it.copy(isSaving = true) }
        
        val caseToUpdate = state.cases.find { it.firestoreDocId == caseId }?.copy(
            caseNumber = state.numeroExpediente,
            CaseTittle = state.tituloCaso,
            processType = state.tipoProceso,
            status = state.estadoCaso,
            description = state.descripcion
        ) ?: return

        viewModelScope.launch {
            val res = repository.updateCase(caseToUpdate)
            if (res.isSuccess) {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
                loadCasos()
            } else {
                _uiState.update { it.copy(isSaving = false, error = res.exceptionOrNull()?.message) }
            }
        }
    }

    fun eliminarCaso() {
        val caseId = _uiState.value.currentCaseId ?: return
        viewModelScope.launch {
            val res = repository.deleteCase(caseId)
            if (res.isSuccess) {
                _uiState.update { it.copy(isSaved = true) }
                loadCasos()
            }
        }
    }

    fun resetSaveState() { _uiState.update { it.copy(isSaved = false) } }
}

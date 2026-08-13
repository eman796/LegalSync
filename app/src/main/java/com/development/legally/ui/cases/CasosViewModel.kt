package com.development.legally.ui.cases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.Case
import com.development.legally.data.model.Client
import com.development.legally.data.repository.CaseRepository
import com.development.legally.data.repository.ClientRepository
import com.google.firebase.Timestamp
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
    
    // Datos para dropdowns
    val availableClients: List<Client> = emptyList(),
    
    // Campos para edición/creación
    val currentCaseId: String? = null,
    val numeroExpediente: String = "",
    val tituloCaso: String = "",
    val tipoProceso: String = "Penal",
    val estadoCaso: String = "Activo",
    val prioridad: String = "Media",
    val descripcion: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class CasosViewModel : ViewModel() {
    private val repository = CaseRepository()
    private val clientRepository = ClientRepository()
    private val _uiState = MutableStateFlow(CasosUiState())
    val uiState: StateFlow<CasosUiState> = _uiState.asStateFlow()

    fun loadCasos() {
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
                _uiState.update { it.copy(error = res.exceptionOrNull()?.message, loading = false) }
            }
        }
    }

    fun loadDropdownData() {
        viewModelScope.launch {
            val clientsRes = clientRepository.getClients()
            _uiState.update { it.copy(availableClients = clientsRes.getOrDefault(emptyList())) }
        }
    }

    fun setCaseForEditing(caseId: String?) {
        loadDropdownData()
        if (caseId == null || caseId == "new") {
            resetForm()
            return
        }
        _uiState.update { it.copy(loading = true, isSaved = false, error = null) }
        viewModelScope.launch {
            val res = repository.getCaseById(caseId)
            res.getOrNull()?.let { populateForm(it) }
            _uiState.update { it.copy(loading = false) }
        }
    }

    private fun populateForm(case: Case) {
        _uiState.update { it.copy(
            currentCaseId = case.firestoreDocId,
            numeroExpediente = case.caseNumber,
            tituloCaso = case.CaseTittle,
            tipoProceso = case.processType.ifEmpty { "Penal" },
            estadoCaso = case.status.ifEmpty { "Activo" },
            prioridad = case.priority.ifEmpty { "Media" },
            descripcion = case.description,
            clientId = case.clientId,
            clientName = case.clientName
        ) }
    }

    fun resetForm() {
        _uiState.update { it.copy(
            currentCaseId = null,
            isSaved = false,
            numeroExpediente = "", tituloCaso = "", tipoProceso = "Penal", estadoCaso = "Activo", prioridad = "Media", descripcion = "",
            clientId = "", clientName = ""
        ) }
    }

    fun onNumeroExpedienteChange(v: String) { _uiState.update { it.copy(numeroExpediente = v) } }
    fun onTituloCasoChange(v: String) { _uiState.update { it.copy(tituloCaso = v) } }
    fun onTipoProcesoChange(v: String) { _uiState.update { it.copy(tipoProceso = v) } }
    fun onEstadoCasoChange(v: String) { _uiState.update { it.copy(estadoCaso = v) } }
    fun onPrioridadChange(v: String) { _uiState.update { it.copy(prioridad = v) } }
    fun onDescripcionChange(v: String) { _uiState.update { it.copy(descripcion = v) } }
    
    fun onClientNameChange(v: String) {
        val client = _uiState.value.availableClients.find { "${it.name} ${it.lastName}" == v }
        if (client != null) {
            _uiState.update { it.copy(clientId = client.id, clientName = v) }
        } else {
            _uiState.update { it.copy(clientName = v) }
        }
    }

    fun guardarCaso() {
        val state = _uiState.value
        if (state.numeroExpediente.isBlank()) return
        
        _uiState.update { it.copy(isSaving = true) }
        
        val caseToSave = Case(
            firestoreDocId = state.currentCaseId ?: "",
            caseNumber = state.numeroExpediente,
            CaseTittle = state.tituloCaso,
            processType = state.tipoProceso,
            status = state.estadoCaso,
            priority = state.prioridad,
            description = state.descripcion,
            clientId = state.clientId,
            clientName = state.clientName,
            updatedAt = Timestamp.now()
        )

        viewModelScope.launch {
            val res = if (state.currentCaseId == null) repository.createCase(caseToSave.copy(createdAt = Timestamp.now()))
                      else repository.updateCase(caseToSave)
            
            if (res.isSuccess) {
                loadCasos()
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } else {
                _uiState.update { it.copy(isSaving = false, error = res.exceptionOrNull()?.message) }
            }
        }
    }

    fun duplicarCaso() {
        val state = _uiState.value
        _uiState.update { it.copy(isSaving = true) }
        
        val caseToDuplicate = Case(
            caseNumber = "${state.numeroExpediente} (Copia)",
            CaseTittle = state.tituloCaso,
            processType = state.tipoProceso,
            status = state.estadoCaso,
            priority = state.prioridad,
            description = state.descripcion,
            clientId = state.clientId,
            clientName = state.clientName,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )

        viewModelScope.launch {
            val res = repository.createCase(caseToDuplicate)
            if (res.isSuccess) {
                loadCasos()
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } else {
                _uiState.update { it.copy(isSaving = false, error = res.exceptionOrNull()?.message) }
            }
        }
    }

    fun eliminarCaso() {
        val caseId = _uiState.value.currentCaseId ?: return
        viewModelScope.launch {
            if (repository.deleteCase(caseId).isSuccess) {
                loadCasos()
                _uiState.update { it.copy(isSaved = true) }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query, filtered = applyFilterLogic(it.cases, query, it.filterStatus, it.filterPriority)) }
    }

    fun updateStatusFilter(status: String) {
        _uiState.update { it.copy(filterStatus = status, filtered = applyFilterLogic(it.cases, it.searchQuery, status, it.filterPriority)) }
    }

    fun updatePriorityFilter(priority: String) {
        _uiState.update { it.copy(filterPriority = priority, filtered = applyFilterLogic(it.cases, it.searchQuery, it.filterStatus, priority)) }
    }

    private fun applyFilterLogic(cases: List<Case>, query: String, status: String, priority: String): List<Case> {
        val q = query.lowercase()
        return cases.filter { c ->
            val matchesQuery = c.caseNumber.lowercase().contains(q) || c.description.lowercase().contains(q) || c.clientName.lowercase().contains(q)
            val matchesStatus = if (status == "Todos") true else c.status == status
            val matchesPriority = if (priority == "Todas") true else c.priority == priority
            matchesQuery && matchesStatus && matchesPriority
        }
    }

    fun resetSaveState() { _uiState.update { it.copy(isSaved = false, isSaving = false) } }
}

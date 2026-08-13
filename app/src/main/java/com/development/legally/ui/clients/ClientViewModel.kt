package com.development.legally.ui.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.Client
import com.development.legally.data.repository.ClientRepository
import com.development.legally.data.repository.CaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClientsUiState(
    val clients: List<Client> = emptyList(),
    val filtered: List<Client> = emptyList(),
    val clientCaseCounts: Map<String, Int> = emptyMap(),
    val selectedClient: Client? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filterPersonType: String = "Todos",
    val filterStatus: String = "Todos",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class ClientViewModel : ViewModel() {

    private val repository = ClientRepository()
    private val caseRepository = CaseRepository()

    private val _uiState = MutableStateFlow(ClientsUiState())
    val uiState: StateFlow<ClientsUiState> = _uiState

    init {
        loadClients()
    }

    fun loadClients() {
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            val res = repository.getClients()
            val casesRes = caseRepository.getCases()
            
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                val cases = casesRes.getOrDefault(emptyList())
                
                // Contar expedientes activos por cliente
                val counts = cases.filter { it.status.lowercase() != "finalizado" && it.status.lowercase() != "archivado" }
                    .groupBy { it.clientId }
                    .mapValues { it.value.size }

                _uiState.update { state ->
                    state.copy(
                        clients = list,
                        clientCaseCounts = counts,
                        loading = false
                    )
                }
                applyFilters()
            } else {
                _uiState.update { it.copy(
                    error = res.exceptionOrNull()?.message,
                    loading = false
                ) }
            }
        }
    }

    fun loadClientsFilteredAndOrdered(filterField: String?, filterValue: String?, orderField: String?) {
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            val res = repository.getClientsFilteredAndOrdered(filterField, filterValue, orderField)
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.update { it.copy(
                    clients = list,
                    filtered = list,
                    loading = false
                ) }
            } else {
                _uiState.update { it.copy(
                    error = res.exceptionOrNull()?.message,
                    loading = false
                ) }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun updatePersonTypeFilter(type: String) {
        _uiState.update { it.copy(filterPersonType = type) }
        applyFilters()
    }

    fun updateStatusFilter(status: String) {
        _uiState.update { it.copy(filterStatus = status) }
        applyFilters()
    }

    private fun applyFilters() {
        val currentState = _uiState.value
        val query = currentState.searchQuery.lowercase()
        val personType = currentState.filterPersonType
        val status = currentState.filterStatus

        val filteredList = currentState.clients.filter { c ->
            val matchesQuery = if (query.isBlank()) true else {
                val fullName = "${c.name} ${c.lastName}".lowercase()
                fullName.contains(query) ||
                        c.email.lowercase().contains(query) ||
                        c.phone.lowercase().contains(query) ||
                        c.address.lowercase().contains(query)
            }

            val matchesType = if (personType == "Todos") true else c.personType == personType
            val matchesStatus = if (status == "Todos") true else status == "Activo"

            matchesQuery && matchesType && matchesStatus
        }

        _uiState.update { it.copy(filtered = filteredList) }
    }

    fun loadClientById(clientId: String, onResult: (Client?) -> Unit) {
        viewModelScope.launch {
            val res = repository.getClientById(clientId)
            if (res.isSuccess) {
                val client = res.getOrNull()
                _uiState.update { it.copy(selectedClient = client) }
                onResult(client)
            } else {
                onResult(null)
            }
        }
    }

    fun createClient(client: Client, onDone: (Boolean, String?) -> Unit) {
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val res = repository.createClient(client)
            if (res.isSuccess) {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
                onDone(true, null)
                loadClients()
            } else {
                _uiState.update { it.copy(isSaving = false) }
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun updateClient(client: Client, onDone: (Boolean, String?) -> Unit) {
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            val res = repository.updateClient(client)
            if (res.isSuccess) {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
                onDone(true, null)
                loadClients()
            } else {
                _uiState.update { it.copy(isSaving = false) }
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun duplicarClient(client: Client, onDone: (Boolean, String?) -> Unit) {
        _uiState.update { it.copy(isSaving = true) }
        val duplicatedClient = client.copy(
            id = "",
            name = "${client.name} (Copia)",
            createdAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            val res = repository.createClient(duplicatedClient)
            if (res.isSuccess) {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
                onDone(true, null)
                loadClients()
            } else {
                _uiState.update { it.copy(isSaving = false) }
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun deleteClient(clientId: String, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.deleteClient(clientId)
            if (res.isSuccess) {
                onDone(true, null)
                loadClients()
            } else {
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun resetSaveState() {
        _uiState.update { it.copy(isSaved = false, isSaving = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
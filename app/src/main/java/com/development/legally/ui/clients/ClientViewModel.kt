package com.development.legally.ui.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.Client
import com.development.legally.data.repository.ClientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ClientsUiState(
    val clients: List<Client> = emptyList(),
    val filtered: List<Client> = emptyList(),
    val selectedClient: Client? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filterPersonType: String = "Todos",
    val filterStatus: String = "Todos"
)

class ClientViewModel : ViewModel() {

    private val repository = ClientRepository()

    private val _uiState = MutableStateFlow(ClientsUiState())
    val uiState: StateFlow<ClientsUiState> = _uiState

    init {
        loadClients()
    }

    fun loadClients() {
        _uiState.value = _uiState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            val res = repository.getClients()
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.value = _uiState.value.copy(
                    clients = list,
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

    fun updatePersonTypeFilter(type: String) {
        _uiState.value = _uiState.value.copy(filterPersonType = type)
        applyFilters()
    }

    fun updateStatusFilter(status: String) {
        _uiState.value = _uiState.value.copy(filterStatus = status)
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
            // Assuming "Activo" for now as Client model doesn't have status yet
            val matchesStatus = if (status == "Todos") true else status == "Activo" 

            matchesQuery && matchesType && matchesStatus
        }

        _uiState.value = _uiState.value.copy(filtered = filteredList)
    }

    fun searchClients(query: String) {
        updateSearchQuery(query)
    }

    fun loadClientById(clientId: String, onResult: (Client?) -> Unit) {
        viewModelScope.launch {
            val res = repository.getClientById(clientId)
            if (res.isSuccess) {
                val client = res.getOrNull()
                _uiState.value = _uiState.value.copy(selectedClient = client)
                onResult(client)
            } else {
                onResult(null)
            }
        }
    }

    fun createClient(client: Client, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.createClient(client)
            if (res.isSuccess) {
                loadClients()
                onDone(true, null)
            } else {
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun updateClient(client: Client, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.updateClient(client)
            if (res.isSuccess) {
                loadClients()
                onDone(true, null)
            } else {
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun deleteClient(clientId: String, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.deleteClient(clientId)
            if (res.isSuccess) {
                loadClients()
                onDone(true, null)
            } else {
                onDone(false, res.exceptionOrNull()?.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSelectedClient() {
        _uiState.value = _uiState.value.copy(selectedClient = null)
    }

    // Ordenar y filtrar usando consultas estructuradas al repositorio (Firestore)
    fun loadClientsOrderedBy(field: String) {
        _uiState.value = _uiState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            val res = repository.getClientsOrderedBy(field)
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.value = _uiState.value.copy(clients = list, filtered = list, loading = false)
            } else {
                _uiState.value = _uiState.value.copy(error = res.exceptionOrNull()?.message, loading = false)
            }
        }
    }

    fun loadClientsFilteredBy(field: String, value: String) {
        _uiState.value = _uiState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            val res = repository.getClientsFilteredBy(field, value)
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.value = _uiState.value.copy(filtered = list, loading = false)
            } else {
                _uiState.value = _uiState.value.copy(error = res.exceptionOrNull()?.message, loading = false)
            }
        }
    }

    fun loadClientsFilteredAndOrdered(filterField: String?, filterValue: String?, orderField: String?) {
        _uiState.value = _uiState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            val res = repository.getClientsFilteredAndOrdered(filterField, filterValue, orderField)
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.value = _uiState.value.copy(clients = list, filtered = list, loading = false)
            } else {
                _uiState.value = _uiState.value.copy(error = res.exceptionOrNull()?.message, loading = false)
            }
        }
    }
}

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
    val searchQuery: String = ""
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
                _uiState.value = ClientsUiState(
                    clients = list,
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

    fun searchClients(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        val filtered = if (query.isBlank()) {
            _uiState.value.clients
        } else {
            _uiState.value.clients.filter { c ->
                val fullName = (c.name + " " + c.lastName).lowercase()
                val email = c.email.lowercase()
                val phone = c.phone.lowercase()
                val queryLower = query.lowercase()
                
                fullName.contains(queryLower) ||
                email.contains(queryLower) ||
                phone.contains(queryLower) ||
                c.address.lowercase().contains(queryLower)
            }
        }
        _uiState.value = _uiState.value.copy(filtered = filtered)
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
}

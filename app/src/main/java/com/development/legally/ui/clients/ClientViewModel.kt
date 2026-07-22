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
    val loading: Boolean = false,
    val error: String? = null
)

class ClientViewModel : ViewModel() {

    private val repository = ClientRepository()

    private val _uiState = MutableStateFlow(ClientsUiState())
    val uiState: StateFlow<ClientsUiState> = _uiState

    private var currentQuery: String = ""

    fun loadClients() {
        _uiState.value = _uiState.value.copy(loading = true)
        viewModelScope.launch {
            val res = repository.getClients()
            if (res.isSuccess) {
                val list = res.getOrNull() ?: emptyList()
                _uiState.value = ClientsUiState(clients = list, filtered = list)
            } else {
                _uiState.value = _uiState.value.copy(error = res.exceptionOrNull()?.message)
            }
        }
    }

    fun filter(query: String) {
        currentQuery = query
        val filtered = if (query.isBlank()) {
            _uiState.value.clients
        } else {
            _uiState.value.clients.filter { c ->
                (c.name + " " + c.lastName).contains(query, ignoreCase = true) ||
                        c.email.contains(query, ignoreCase = true) ||
                        c.phone.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(filtered = filtered)
    }

    fun loadClientById(clientId: String, onResult: (Client?) -> Unit) {
        viewModelScope.launch {
            val res = repository.getClientById(clientId)
            if (res.isSuccess) onResult(res.getOrNull()) else onResult(null)
        }
    }

    fun createClient(client: Client, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.createClient(client)
            if (res.isSuccess) onDone(true, null) else onDone(false, res.exceptionOrNull()?.message)
        }
    }

    fun updateClient(client: Client, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.updateClient(client)
            if (res.isSuccess) onDone(true, null) else onDone(false, res.exceptionOrNull()?.message)
        }
    }

    fun deleteClient(clientId: String, onDone: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val res = repository.deleteClient(clientId)
            if (res.isSuccess) onDone(true, null) else onDone(false, res.exceptionOrNull()?.message)
        }
    }
}

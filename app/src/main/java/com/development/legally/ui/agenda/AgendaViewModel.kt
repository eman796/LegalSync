package com.development.legally.ui.agenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.Case
import com.development.legally.data.model.Client
import com.development.legally.data.model.Event
import com.development.legally.data.repository.CaseRepository
import com.development.legally.data.repository.ClientRepository
import com.development.legally.data.repository.EventRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class AgendaUiState(
    val events: List<Event> = emptyList(),
    val filteredEvents: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filterDay: String = "Esta semana",
    val filterType: String = "Todos",
    
    val availableCases: List<Case> = emptyList(),
    val availableClients: List<Client> = emptyList(),
    
    val currentEventId: String? = null,
    val titulo: String = "",
    val tipo: String = "Audiencia",
    val estado: String = "Disponible",
    val fechaHora: String = "",
    val duracion: String = "",
    val lugar: String = "",
    val descripcion: String = "",
    val casoRelacionado: String = "",
    val participante: String = "",
    val repetir: String = "Nunca",
    val recordar: String = "Sin aviso",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class AgendaViewModel(
    private val repository: EventRepository = EventRepository(),
    private val caseRepository: CaseRepository = CaseRepository(),
    private val clientRepository: ClientRepository = ClientRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(AgendaUiState())
    val uiState: StateFlow<AgendaUiState> = _uiState.asStateFlow()

    private val fullDateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun loadEvents() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = repository.getEvents()
            val list = result.getOrDefault(emptyList())
            _uiState.update { it.copy(
                events = list,
                isLoading = false,
                filteredEvents = applyFilterLogic(list, it.searchQuery, it.filterType)
            ) }
        }
    }

    fun loadDropdownData() {
        viewModelScope.launch {
            val cases = caseRepository.getCases().getOrDefault(emptyList())
            val clients = clientRepository.getClients().getOrDefault(emptyList())
            _uiState.update { it.copy(availableCases = cases, availableClients = clients) }
        }
    }

    fun setEventForEditing(eventId: String?) {
        // Fix: Reset state to force fresh load and clear previous success flags
        _uiState.update { currentState ->
            AgendaUiState(
                isLoading = true,
                availableCases = currentState.availableCases,
                availableClients = currentState.availableClients,
                currentEventId = if (eventId == "new" || eventId == null) null else eventId
            )
        }
        
        loadDropdownData()
        
        if (eventId == null || eventId == "new") {
            _uiState.update { it.copy(isLoading = false) }
            return
        }
        
        viewModelScope.launch {
            val result = repository.getEventById(eventId)
            result.getOrNull()?.let { event ->
                _uiState.update { it.copy(
                    titulo = event.titulo,
                    tipo = event.tipo.ifEmpty { "Audiencia" },
                    estado = event.estado.ifEmpty { "Disponible" },
                    fechaHora = event.fechaHora?.let { fullDateFormatter.format(it.toDate()) } ?: "",
                    duracion = event.duracion,
                    lugar = event.lugar,
                    descripcion = event.descripcion,
                    casoRelacionado = event.casoRelacionado,
                    participante = event.participante,
                    repetir = event.repetir.ifEmpty { "Nunca" },
                    recordar = event.recordar.ifEmpty { "Sin aviso" },
                    isLoading = false
                ) }
            } ?: _uiState.update { it.copy(isLoading = false, error = "Evento no encontrado") }
        }
    }

    fun guardarEvento() {
        val state = _uiState.value
        if (state.titulo.isBlank()) return
        
        _uiState.update { it.copy(isSaving = true) }
        
        val timestamp = try {
            val date = fullDateFormatter.parse(state.fechaHora)
            Timestamp(date ?: Date())
        } catch (e: Exception) { 
            Timestamp.now() 
        }

        val event = Event(
            eventId = state.currentEventId ?: "",
            id = state.currentEventId ?: "",
            titulo = state.titulo,
            tipo = state.tipo,
            estado = state.estado,
            fechaHora = timestamp,
            duracion = state.duracion,
            lugar = state.lugar,
            descripcion = state.descripcion,
            casoRelacionado = state.casoRelacionado,
            participante = state.participante,
            repetir = state.repetir,
            recordar = state.recordar
        )

        viewModelScope.launch {
            val res = if (!state.currentEventId.isNullOrEmpty()) repository.updateEvent(event) else repository.createEvent(event)
            if (res.isSuccess) {
                loadEvents()
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } else {
                _uiState.update { it.copy(isSaving = false, error = res.exceptionOrNull()?.message) }
            }
        }
    }

    fun formatEventTimeRange(event: Event): String {
        val timestamp = event.fechaHora ?: return event.duracion
        val durStr = event.duracion
        
        val date = timestamp.toDate()
        val startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        
        val calendar = Calendar.getInstance()
        calendar.time = date
        
        val durLower = durStr.lowercase()
        val amount = durLower.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
        
        if (amount > 0) {
            if (durLower.contains("min")) {
                calendar.add(Calendar.MINUTE, amount)
            } else if (durLower.contains("hora") || durLower.contains("hour")) {
                calendar.add(Calendar.HOUR_OF_DAY, amount)
            }
        }
        
        val endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
        return if (durStr.isNotEmpty()) "$durStr - $startTime hasta $endTime" else "$startTime hasta $endTime"
    }

    fun onTituloChange(v: String) { _uiState.update { it.copy(titulo = v) } }
    fun onTipoChange(v: String) { _uiState.update { it.copy(tipo = v) } }
    fun onEstadoChange(v: String) { _uiState.update { it.copy(estado = v) } }
    fun onFechaHoraChange(v: String) { _uiState.update { it.copy(fechaHora = v) } }
    fun onDuracionChange(v: String) { _uiState.update { it.copy(duracion = v) } }
    fun onLugarChange(v: String) { _uiState.update { it.copy(lugar = v) } }
    fun onDescripcionChange(v: String) { _uiState.update { it.copy(descripcion = v) } }
    fun onCasoRelacionadoChange(v: String) { _uiState.update { it.copy(casoRelacionado = v) } }
    fun onParticipanteChange(v: String) { _uiState.update { it.copy(participante = v) } }
    fun onRepetirChange(v: String) { _uiState.update { it.copy(repetir = v) } }
    fun onRecordarChange(v: String) { _uiState.update { it.copy(recordar = v) } }
    fun resetSaveState() { _uiState.update { it.copy(isSaved = false) } }
    
    fun updateSearchQuery(q: String) {
        _uiState.update { it.copy(searchQuery = q, filteredEvents = applyFilterLogic(it.events, q, it.filterType)) }
    }

    fun updateTypeFilter(t: String) {
        _uiState.update { it.copy(filterType = t, filteredEvents = applyFilterLogic(it.events, it.searchQuery, t)) }
    }

    private fun applyFilterLogic(events: List<Event>, query: String, type: String): List<Event> {
        val q = query.lowercase()
        return events.filter { 
            (it.titulo.lowercase().contains(q) || it.descripcion.lowercase().contains(q) || it.casoRelacionado.lowercase().contains(q)) &&
            (if (type == "Todos") true else it.tipo == type)
        }
    }

    fun updateDayFilter(d: String) { _uiState.update { it.copy(filterDay = d) } }
    
    fun eliminarEvento() { 
        viewModelScope.launch {
            _uiState.value.currentEventId?.let { repository.deleteEvent(it) }
            loadEvents()
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}

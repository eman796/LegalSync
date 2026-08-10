package com.development.legally.ui.agenda

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.Event
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
    
    // Campos para el formulario (Nuevo/Editar)
    val currentEventId: String? = null,
    val titulo: String = "",
    val tipo: String = "",
    val estado: String = "",
    val fechaHora: String = "", 
    val duracion: String = "",
    val lugar: String = "",
    val descripcion: String = "",
    val casoRelacionado: String = "",
    val repetir: String = "",
    val recordar: String = "",
    val participante: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class AgendaViewModel(private val repository: EventRepository = EventRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(AgendaUiState())
    val uiState: StateFlow<AgendaUiState> = _uiState.asStateFlow()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun loadEvents() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            val result = repository.getEvents()
            if (result.isSuccess) {
                val list = result.getOrNull() ?: emptyList()
                _uiState.update { state ->
                    state.copy(
                        events = list,
                        isLoading = false,
                        filteredEvents = applyFilterLogic(list, state.searchQuery, state.filterType)
                    )
                }
            } else {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Error al cargar la agenda"
                ) }
            }
        }
    }

    // Nueva función para cargar un evento específico para edición
    fun setEventForEditing(eventId: String?) {
        if (eventId == null) {
            resetForm()
            return
        }
        
        // Usamos eventId (que es el firestore Doc ID) para buscar en la lista
        val event = _uiState.value.events.find { it.eventId == eventId }
        if (event != null) {
            _uiState.update { it.copy(
                currentEventId = event.eventId,
                titulo = event.titulo,
                tipo = event.tipo,
                estado = event.estado,
                fechaHora = event.fechaHora?.let { dateFormat.format(it.toDate()) } ?: "",
                duracion = event.duracion,
                lugar = event.lugar,
                descripcion = event.descripcion,
                casoRelacionado = event.casoRelacionado,
                repetir = event.repetir,
                recordar = event.recordar,
                participante = event.participante
            ) }
        } else {
            // Si no está en la lista (ej: link directo), lo buscamos en el repo
            viewModelScope.launch {
                val result = repository.getEventById(eventId)
                result.getOrNull()?.let { remoteEvent ->
                    _uiState.update { it.copy(
                        currentEventId = remoteEvent.eventId,
                        titulo = remoteEvent.titulo,
                        tipo = remoteEvent.tipo,
                        estado = remoteEvent.estado,
                        fechaHora = remoteEvent.fechaHora?.let { dateFormat.format(it.toDate()) } ?: "",
                        duracion = remoteEvent.duracion,
                        lugar = remoteEvent.lugar,
                        descripcion = remoteEvent.descripcion,
                        casoRelacionado = remoteEvent.casoRelacionado,
                        repetir = remoteEvent.repetir,
                        recordar = remoteEvent.recordar,
                        participante = remoteEvent.participante
                    ) }
                }
            }
        }
    }

    private fun resetForm() {
        _uiState.update { it.copy(
            currentEventId = null,
            titulo = "", tipo = "", estado = "", fechaHora = "", duracion = "",
            lugar = "", descripcion = "", casoRelacionado = "", repetir = "", recordar = "", participante = ""
        ) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredEvents = applyFilterLogic(state.events, query, state.filterType)
            )
        }
    }

    fun updateTypeFilter(type: String) {
        _uiState.update { state ->
            state.copy(
                filterType = type,
                filteredEvents = applyFilterLogic(state.events, state.searchQuery, type)
            )
        }
    }

    private fun applyFilterLogic(events: List<Event>, query: String, type: String): List<Event> {
        val q = query.lowercase()
        return events.filter { event ->
            val matchesQuery = event.titulo.lowercase().contains(q) ||
                             event.descripcion.lowercase().contains(q) ||
                             event.casoRelacionado.lowercase().contains(q)

            val matchesType = if (type == "Todos") true else event.tipo == type
            matchesQuery && matchesType
        }
    }

    fun onTituloChange(v: String) { _uiState.update { it.copy(titulo = v) } }
    fun onTipoChange(v: String) { _uiState.update { it.copy(tipo = v) } }
    fun onEstadoChange(v: String) { _uiState.update { it.copy(estado = v) } }
    fun onFechaHoraChange(v: String) { _uiState.update { it.copy(fechaHora = v) } }
    fun onDuracionChange(v: String) { _uiState.update { it.copy(duracion = v) } }
    fun onLugarChange(v: String) { _uiState.update { it.copy(lugar = v) } }
    fun onDescripcionChange(v: String) { _uiState.update { it.copy(descripcion = v) } }
    fun onCasoRelacionadoChange(v: String) { _uiState.update { it.copy(casoRelacionado = v) } }
    fun onRepetirChange(v: String) { _uiState.update { it.copy(repetir = v) } }
    fun onRecordarChange(v: String) { _uiState.update { it.copy(recordar = v) } }
    fun onParticipanteChange(v: String) { _uiState.update { it.copy(participante = v) } }

    fun guardarEvento() {
        val state = _uiState.value
        if (state.titulo.isBlank()) return
        
        _uiState.update { it.copy(isSaving = true) }

        val timestamp = try {
            val date = dateFormat.parse(state.fechaHora)
            if (date != null) Timestamp(date) else Timestamp.now()
        } catch (e: Exception) {
            Timestamp.now()
        }

        val eventToSave = Event(
            eventId = state.currentEventId ?: "",
            titulo = state.titulo,
            tipo = state.tipo,
            estado = state.estado,
            fechaHora = timestamp,
            duracion = state.duracion,
            lugar = state.lugar,
            descripcion = state.descripcion,
            casoRelacionado = state.casoRelacionado,
            repetir = state.repetir,
            recordar = state.recordar,
            participante = state.participante
        )

        viewModelScope.launch {
            val res = if (state.currentEventId != null) {
                repository.updateEvent(eventToSave)
            } else {
                repository.createEvent(eventToSave)
            }
            
            if (res.isSuccess) {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
                loadEvents()
            } else {
                _uiState.update { it.copy(isSaving = false, error = res.exceptionOrNull()?.message) }
            }
        }
    }

    fun eliminarEvento() {
        val eventId = _uiState.value.currentEventId ?: return
        viewModelScope.launch {
            val res = repository.deleteEvent(eventId)
            if (res.isSuccess) {
                _uiState.update { it.copy(isSaved = true) }
                loadEvents()
            }
        }
    }

    fun resetSaveState() { _uiState.update { it.copy(isSaved = false) } }
    fun updateDayFilter(d: String) { _uiState.update { it.copy(filterDay = d) } }
}

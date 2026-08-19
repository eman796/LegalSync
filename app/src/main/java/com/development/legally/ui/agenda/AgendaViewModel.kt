package com.development.legally.ui.agenda

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.model.Case
import com.development.legally.data.model.Client
import com.development.legally.data.model.Event
import com.development.legally.data.model.NotificationItem
import com.development.legally.data.repository.CaseRepository
import com.development.legally.data.repository.ClientRepository
import com.development.legally.data.repository.EventRepository
import com.development.legally.core.notifications.NotificationHelper
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
    val notifications: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filterDay: String = "Todos",
    val filterType: String = "Todos",
    
    val availableCases: List<Case> = emptyList(),
    val availableClients: List<Client> = emptyList(),
    
    val currentEventId: String? = null,
    val titulo: String = "",
    val tipo: String = "Audiencia",
    val estado: String = "Disponible",
    val fechaHora: String = "",
    val duracion: String = "1 hora",
    val lugar: String = "",
    val descripcion: String = "",
    val casoRelacionado: String = "",
    val participante: String = "",
    val repetir: String = "Nunca",
    val recordar: String = "Sin aviso",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class AgendaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EventRepository()
    private val caseRepository = CaseRepository()
    private val clientRepository = ClientRepository()
    
    private val _uiState = MutableStateFlow(AgendaUiState())
    val uiState: StateFlow<AgendaUiState> = _uiState.asStateFlow()

    private val fullDateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun loadEvents() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = repository.getEvents()
            val list = result.getOrDefault(emptyList())
            _uiState.update { state ->
                state.copy(
                    events = list,
                    isLoading = false,
                    filteredEvents = applyFilterLogic(list, state.searchQuery, state.filterType, state.filterDay),
                    notifications = generateNotifications(list)
                )
            }
        }
    }

    private fun generateNotifications(events: List<Event>): List<NotificationItem> {
        val now = System.currentTimeMillis()
        return events.filter { it.fechaHora != null && it.recordar != "Sin aviso" }
            .map { event ->
                val date = event.fechaHora!!.toDate()
                NotificationItem(
                    id = event.eventId,
                    title = "Aviso: ${event.titulo}",
                    message = "${event.tipo} programada para las ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)}",
                    eventId = event.eventId,
                    timestamp = date.time
                )
            }
            .sortedByDescending { it.timestamp }
    }

    fun loadDropdownData() {
        viewModelScope.launch {
            val cases = caseRepository.getCases().getOrDefault(emptyList())
            val clients = clientRepository.getClients().getOrDefault(emptyList())
            _uiState.update { it.copy(availableCases = cases, availableClients = clients) }
        }
    }

    fun setEventForEditing(eventId: String?) {
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
            resetForm()
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
                    duracion = event.duracion.ifEmpty { "1 hora" },
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

    private fun resetForm() {
        _uiState.update { it.copy(
            titulo = "", tipo = "Audiencia", estado = "Disponible",
            fechaHora = "", duracion = "1 hora", lugar = "", descripcion = "",
            casoRelacionado = "", participante = "", repetir = "Nunca", recordar = "Sin aviso"
        ) }
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
            val res = if (state.currentEventId == null) repository.createEvent(event) else repository.updateEvent(event)
            if (res.isSuccess) {
                if (event.recordar != "Sin aviso") {
                    NotificationHelper.scheduleEventNotification(getApplication(), event)
                }
                loadEvents()
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } else {
                _uiState.update { it.copy(isSaving = false, error = res.exceptionOrNull()?.message) }
            }
        }
    }

    fun duplicarEvento() {
        val state = _uiState.value
        _uiState.update { it.copy(isSaving = true) }
        
        val timestamp = try {
            val date = fullDateFormatter.parse(state.fechaHora)
            Timestamp(date ?: Date())
        } catch (e: Exception) { Timestamp.now() }

        val event = Event(
            titulo = "${state.titulo} (Copia)",
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
            if (repository.createEvent(event).isSuccess) {
                loadEvents()
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
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
    
    fun resetSaveState() { _uiState.update { it.copy(isSaved = false, isSaving = false) } }
    
    fun updateSearchQuery(q: String) {
        _uiState.update { it.copy(searchQuery = q) }
        _uiState.update { it.copy(filteredEvents = applyFilterLogic(it.events, q, it.filterType, it.filterDay)) }
    }

    fun updateTypeFilter(t: String) {
        _uiState.update { it.copy(filterType = t) }
        _uiState.update { it.copy(filteredEvents = applyFilterLogic(it.events, it.searchQuery, t, it.filterDay)) }
    }

    fun updateDayFilter(d: String) {
        _uiState.update { it.copy(filterDay = d) }
        _uiState.update { it.copy(filteredEvents = applyFilterLogic(it.events, it.searchQuery, it.filterType, d)) }
    }

    private fun applyFilterLogic(events: List<Event>, query: String, type: String, day: String): List<Event> {
        val q = query.lowercase()
        
        val todayCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val todayStart = todayCal.timeInMillis
        
        val tomorrowCal = (todayCal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }
        val tomorrowStart = tomorrowCal.timeInMillis
        
        val dayAfterTomorrowStart = (tomorrowCal.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, 1) }.timeInMillis
        
        val weekCal = (todayCal.clone() as Calendar).apply { set(Calendar.DAY_OF_WEEK, firstDayOfWeek) }
        val weekStart = weekCal.timeInMillis
        val weekEnd = weekStart + (7 * 24 * 60 * 60 * 1000)

        val monthCal = (todayCal.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
        val monthStart = monthCal.timeInMillis
        val monthEnd = (monthCal.clone() as Calendar).apply { add(Calendar.MONTH, 1) }.timeInMillis

        return events.filter { event ->
            val matchesQuery = (event.titulo.lowercase().contains(q) || event.descripcion.lowercase().contains(q) || (event.casoRelacionado ?: "").lowercase().contains(q))
            val matchesType = (if (type == "Todos") true else event.tipo == type)
            
            val matchesDay = if (event.fechaHora != null && day != "Todos") {
                val eventTime = event.fechaHora!!.toDate().time
                when (day) {
                    "Hoy" -> eventTime in todayStart until tomorrowStart
                    "Mañana" -> eventTime in tomorrowStart until dayAfterTomorrowStart
                    "Esta semana" -> eventTime in weekStart until weekEnd
                    "Mes" -> eventTime in monthStart until monthEnd
                    else -> true
                }
            } else true

            matchesQuery && matchesType && matchesDay
        }
    }

    fun eliminarEvento() { 
        viewModelScope.launch {
            val eventId = _uiState.value.currentEventId
            if (eventId != null) {
                repository.deleteEvent(eventId)
                NotificationHelper.cancelNotification(getApplication(), eventId)
                loadEvents()
                _uiState.update { it.copy(isSaved = true) }
            }
        }
    }
}

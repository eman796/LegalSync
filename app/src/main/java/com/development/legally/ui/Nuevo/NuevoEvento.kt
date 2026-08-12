package com.development.legally.ui.Nuevo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.*
import com.development.legally.ui.agenda.AgendaViewModel
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun NuevoEventoScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: AgendaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar datos necesarios al iniciar y resetear formulario
    LaunchedEffect(Unit) {
        viewModel.setEventForEditing("new")
    }

    // Navegar atrás cuando se guarda con éxito
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSave()
            viewModel.resetSaveState()
        }
    }

    NuevoEventoContent(
        titulo = uiState.titulo,
        tipo = uiState.tipo,
        estado = uiState.estado,
        fechaHora = uiState.fechaHora,
        duracion = uiState.duracion,
        lugar = uiState.lugar,
        descripcion = uiState.descripcion,
        casoRelacionado = uiState.casoRelacionado,
        participante = uiState.participante,
        repetir = uiState.repetir,
        recordar = uiState.recordar,
        availableCases = uiState.availableCases.map { it.caseNumber },
        availableClients = uiState.availableClients.map { "${it.name} ${it.lastName}" },
        isSaving = uiState.isSaving,
        onBack = onBack,
        onSaveClick = { viewModel.guardarEvento() },
        onTituloChange = { viewModel.onTituloChange(it) },
        onTipoChange = { viewModel.onTipoChange(it) },
        onEstadoChange = { viewModel.onEstadoChange(it) },
        onFechaHoraChange = { viewModel.onFechaHoraChange(it) },
        onDuracionChange = { viewModel.onDuracionChange(it) },
        onLugarChange = { viewModel.onLugarChange(it) },
        onDescripcionChange = { viewModel.onDescripcionChange(it) },
        onCasoRelacionadoChange = { viewModel.onCasoRelacionadoChange(it) },
        onParticipanteChange = { viewModel.onParticipanteChange(it) },
        onRepetirChange = { viewModel.onRepetirChange(it) },
        onRecordarChange = { viewModel.onRecordarChange(it) }
    )
}

@Composable
fun NuevoEventoContent(
    titulo: String,
    tipo: String,
    estado: String,
    fechaHora: String,
    duracion: String,
    lugar: String,
    descripcion: String,
    casoRelacionado: String,
    participante: String,
    repetir: String,
    recordar: String,
    availableCases: List<String>,
    availableClients: List<String>,
    isSaving: Boolean,
    onBack: () -> Unit,
    onSaveClick: () -> Unit,
    onTituloChange: (String) -> Unit,
    onTipoChange: (String) -> Unit,
    onEstadoChange: (String) -> Unit,
    onFechaHoraChange: (String) -> Unit,
    onDuracionChange: (String) -> Unit,
    onLugarChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCasoRelacionadoChange: (String) -> Unit,
    onParticipanteChange: (String) -> Unit,
    onRepetirChange: (String) -> Unit,
    onRecordarChange: (String) -> Unit
) {
    val tiposOptions = listOf("Audiencia", "Cita", "Reunión", "Juicio", "Visita", "Otro")
    val estadosOptions = listOf("Disponible", "Ocupado", "Pendiente", "Completado")
    val duracionesOptions = listOf("15 min", "30 min", "1 hora", "2 horas", "Todo el día")
    val repetirOptions = listOf("Nunca", "Diariamente", "Semanalmente", "Mensualmente")
    val recordarOptions = listOf("Sin aviso", "5 min antes", "15 min antes", "30 min antes", "1 hora antes")

    BaseFormScreen(
        title = "Nuevo evento",
        saveButtonLabel = if (isSaving) "Guardando..." else "Guardar evento",
        onBackClick = onBack,
        onCancelConfirm = onBack,
        onSaveClick = onSaveClick
    ) {
        FormSectionHeader(
            title = "Información General",
            icon = { Icon(painterResource(id = R.drawable.ic_expedientes_edit), null, tint = Color(0xFF9E8D44)) }
        )

        FormElement("Título del Evento *", "Ej: Audiencia Preliminar", FormDataType.STRING, titulo, onTituloChange)

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            FormElement("Tipo de evento", "Seleccionar", FormDataType.LIST, tipo, onTipoChange, modifier = Modifier.weight(1f), options = tiposOptions)
            Spacer(Modifier.width(16.dp))
            FormElement("Estado", "Seleccionar", FormDataType.LIST, estado, onEstadoChange, modifier = Modifier.weight(1f), options = estadosOptions)
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            // Unificado Fecha y Hora en un solo FormElement de tipo DATETIME
            FormElement("Fecha y hora", "dd/MM/yyyy HH:mm", FormDataType.DATETIME, fechaHora, onFechaHoraChange, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            FormElement("Duración", "Seleccionar", FormDataType.LIST, duracion, onDuracionChange, modifier = Modifier.weight(1f), options = duracionesOptions)
        }

        Spacer(Modifier.height(16.dp))

        FormElement("Lugar del evento", "Ingrese ubicación...", FormDataType.STRING, lugar, onLugarChange)

        Spacer(Modifier.height(16.dp))

        FormElement(
            label = "Descripción del evento",
            placeholder = "Detalles adicionales...",
            type = FormDataType.STRING,
            value = descripcion,
            onValueChange = onDescripcionChange,
            height = 120.dp,
            maxChars = 500
        )

        Spacer(Modifier.height(16.dp))

        FormElement("Caso relacionado", "Vincular expediente...", FormDataType.LIST, casoRelacionado, onCasoRelacionadoChange, options = availableCases)

        FormSectionHeader(title = "Quienes participan")

        FormElement("Participantes", "Seleccionar cliente...", FormDataType.LIST, participante, onParticipanteChange, options = availableClients)

        FormSectionHeader(title = "Repeticiones y Avisos")

        Row(Modifier.fillMaxWidth()) {
            FormElement("Repetir", "Nunca", FormDataType.LIST, repetir, onRepetirChange, modifier = Modifier.weight(1f), options = repetirOptions)
            Spacer(Modifier.width(16.dp))
            FormElement("Recordar antes de:", "Sin aviso", FormDataType.LIST, recordar, onRecordarChange, modifier = Modifier.weight(1f), options = recordarOptions)
        }

        Spacer(Modifier.height(24.dp))
    }
}

package com.development.legally.ui.Nuevo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.*
import com.development.legally.ui.agenda.AgendaViewModel
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun NuevoEventoScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: AgendaViewModel = viewModel() // Usamos el ViewModel unificado de Agenda
) {
    val uiState by viewModel.uiState.collectAsState()

    // Efecto para navegar hacia atrás cuando se guarda con éxito
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
        repetir = uiState.repetir,
        recordar = uiState.recordar,
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
    repetir: String,
    recordar: String,
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
    onRepetirChange: (String) -> Unit,
    onRecordarChange: (String) -> Unit
) {
    val tiposOptions = listOf("Audiencia", "Cita", "Reunión", "Juicio", "Visita", "Otro")
    val estadosOptions = listOf("Pendiente", "En Progreso", "Completado", "Cancelado", "Ocupado")
    val duracionesOptions = listOf("30 min", "1 hora", "2 horas", "Todo el día")
    val lugaresOptions = listOf("Oficina Principal", "Juzgado de San José", "Penal La Reforma", "Virtual", "Otro")
    val casosOptions = listOf("25-000044-033-PE - Peculado", "En desarrollo...")
    val repetirOptions = listOf("Nunca", "Diariamente", "Semanalmente", "Mensualmente")
    val recordarOptions = listOf("Sin aviso", "15 min antes", "30 min antes", "1 hora antes", "24 horas antes")

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

        FormElement("Título del Evento", "Ej: Audiencia Preliminar", FormDataType.STRING, titulo, onTituloChange)

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            FormElement("Tipo de evento", "Seleccionar", FormDataType.LIST, tipo, onTipoChange, modifier = Modifier.weight(1f), options = tiposOptions)
            Spacer(Modifier.width(16.dp))
            FormElement("Estado", "Seleccionar", FormDataType.LIST, estado, onEstadoChange, modifier = Modifier.weight(1f), options = estadosOptions)
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            FormElement("Fecha y hora", "00/00/0000 00:00", FormDataType.DATETIME, fechaHora, onFechaHoraChange, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            FormElement("Duración", "Seleccionar", FormDataType.LIST, duracion, onDuracionChange, modifier = Modifier.weight(1f), options = duracionesOptions)
        }

        Spacer(Modifier.height(16.dp))

        FormElement("Lugar del evento", "Seleccionar lugar", FormDataType.LIST, lugar, onLugarChange, options = lugaresOptions)

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

        FormElement("Caso relacionado", "Vincular expediente...", FormDataType.LIST, casoRelacionado, onCasoRelacionadoChange, options = casosOptions)

        FormSectionHeader(title = "Quienes participan")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF171E27), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(48.dp).background(Color(0xFF0D1117), RoundedCornerShape(24.dp)))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Emanuel Calvo", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Cédula: 5-0456-0691", color = Color.Gray, fontSize = 12.sp)
                }
                Icon(Icons.Default.Add, null, tint = Color.Gray, modifier = Modifier.size(32.dp))
            }
        }

        FormSectionHeader(title = "Repeticiones")

        Row(Modifier.fillMaxWidth()) {
            FormElement("Repetir", "Nunca", FormDataType.LIST, repetir, onRepetirChange, modifier = Modifier.weight(1f), options = repetirOptions)
            Spacer(Modifier.width(16.dp))
            FormElement("Recordar antes de:", "Sin aviso", FormDataType.LIST, recordar, onRecordarChange, modifier = Modifier.weight(1f), options = recordarOptions)
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun NuevoEventoScreenPreview() {
    LegallyTheme {
        NuevoEventoContent(
            titulo = "",
            tipo = "",
            estado = "",
            fechaHora = "",
            duracion = "",
            lugar = "",
            descripcion = "",
            casoRelacionado = "",
            repetir = "",
            recordar = "",
            isSaving = false,
            onBack = {},
            onSaveClick = {},
            onTituloChange = {},
            onTipoChange = {},
            onEstadoChange = {},
            onFechaHoraChange = {},
            onDuracionChange = {},
            onLugarChange = {},
            onDescripcionChange = {},
            onCasoRelacionadoChange = {},
            onRepetirChange = {},
            onRecordarChange = {}
        )
    }
}

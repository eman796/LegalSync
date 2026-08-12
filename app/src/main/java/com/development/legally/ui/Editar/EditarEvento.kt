package com.development.legally.ui.Editar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.EdicionSuprema
import com.development.legally.ui.agenda.AgendaViewModel

@Composable
fun EditarEventoScreen(
    eventId: String?,
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: AgendaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val tiposOptions = listOf("Audiencia", "Cita", "Reunión", "Juicio", "Visita", "Otro")
    val estadosOptions = listOf("Disponible", "Ocupado", "Pendiente", "Completado")
    val duracionesOptions = listOf("15 min", "30 min", "1 hora", "2 horas", "Todo el día")
    val lugaresOptions = listOf("Oficina Principal", "Juzgado", "Virtual", "Otro")
    val repetirOptions = listOf("Nunca", "Diariamente", "Semanalmente", "Mensualmente")
    val recordarOptions = listOf("Sin aviso", "5 min antes", "15 min antes", "30 min antes", "1 hora antes")

    LaunchedEffect(eventId) {
        viewModel.setEventForEditing(eventId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSave()
            viewModel.resetSaveState()
        }
    }

    EdicionSuprema.PantallaBase(
        titulo = "Editar evento",
        textoBotonGuardar = if (uiState.isSaving) "Guardando..." else "Guardar cambios",
        textoBotonEliminar = "Eliminar evento",
        onAtras = onBack,
        onCancelar = onBack,
        onGuardar = { viewModel.guardarEvento() },
        onEliminar = { viewModel.eliminarEvento() }
    ) {
        EdicionSuprema.TituloSeccion(
            titulo = "Información General",
            logo = R.drawable.ic_expedientes_edit
        )

        EdicionSuprema.ElementoEdicion(
            titulo = "Título del Evento *",
            placeholder = "Ingrese título...",
            valor = uiState.titulo,
            onValorChange = { viewModel.onTituloChange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Tipo de evento",
                placeholder = "Seleccionar",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.tipo,
                options = tiposOptions,
                onValorChange = { viewModel.onTipoChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Estado",
                placeholder = "Seleccionar",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.estado,
                options = estadosOptions,
                onValorChange = { viewModel.onEstadoChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Fecha y hora",
                placeholder = "Seleccionar fecha...",
                tipo = EdicionSuprema.TipoDato.FECHA,
                valor = uiState.fechaHora,
                onValorChange = { viewModel.onFechaHoraChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Duración",
                placeholder = "Seleccionar",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.duracion,
                options = duracionesOptions,
                onValorChange = { viewModel.onDuracionChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Lugar del evento",
            placeholder = "Seleccione ubicación...",
            tipo = EdicionSuprema.TipoDato.STRING,
            valor = uiState.lugar,
            onValorChange = { viewModel.onLugarChange(it) },
            leadingIcon = { Icon(painterResource(R.drawable.ic_location_pin), null, tint = Color.White, modifier = Modifier.size(20.dp)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Descripción del evento",
            placeholder = "Ingrese descripción...",
            valor = uiState.descripcion,
            onValorChange = { viewModel.onDescripcionChange(it) },
            height = 120.dp,
            maxChars = 500
        )

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Caso relacionado",
            placeholder = "Seleccione caso...",
            tipo = EdicionSuprema.TipoDato.LISTA,
            valor = uiState.casoRelacionado,
            options = uiState.availableCases.map { it.caseNumber },
            onValorChange = { viewModel.onCasoRelacionadoChange(it) }
        )

        EdicionSuprema.TituloSeccion(titulo = "Quienes participan")

        EdicionSuprema.ElementoEdicion(
            titulo = "Participantes",
            placeholder = "Seleccione participantes...",
            tipo = EdicionSuprema.TipoDato.LISTA,
            valor = uiState.participante,
            options = uiState.availableClients.map { "${it.name} ${it.lastName}" },
            onValorChange = { viewModel.onParticipanteChange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Repetir",
                placeholder = "Nunca",
                tipo = EdicionSuprema.TipoDato.LISTA,
                options = repetirOptions,
                valor = uiState.repetir,
                onValorChange = { viewModel.onRepetirChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Recordar antes de:",
                placeholder = "Sin aviso",
                tipo = EdicionSuprema.TipoDato.LISTA,
                options = recordarOptions,
                valor = uiState.recordar,
                onValorChange = { viewModel.onRecordarChange(it) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

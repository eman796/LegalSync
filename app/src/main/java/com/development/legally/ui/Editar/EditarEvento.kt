package com.development.legally.ui.Editar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.development.legally.ui.ClasesSupremas.EdicionSuprema
import com.development.legally.ui.agenda.AgendaViewModel
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun EditarEventoScreen(
    eventId: String?,
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: AgendaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargamos los datos reales de Firebase usando el eventId
    LaunchedEffect(eventId) {
        if (eventId != null) {
            viewModel.setEventForEditing(eventId)
        }
    }

    // Regresar cuando el guardado sea exitoso
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


        // IMPORTANTE: Aquí ya no hay datos dummies, usamos uiState directamente
        EdicionSuprema.ElementoEdicion(
            titulo = "Título del Evento",
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
                onValorChange = { viewModel.onTipoChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Estado",
                placeholder = "Seleccionar",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.estado,
                onValorChange = { viewModel.onEstadoChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Fecha y hora",
                placeholder = "dd/MM/yyyy HH:mm",
                tipo = EdicionSuprema.TipoDato.LISTA,
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
                onValorChange = { viewModel.onDuracionChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Lugar del evento",
            placeholder = "Seleccione ubicación...",
            tipo = EdicionSuprema.TipoDato.LISTA,
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
            onValorChange = { viewModel.onCasoRelacionadoChange(it) }
        )

        EdicionSuprema.TituloSeccion(titulo = "Quienes participan")

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
                    // Muestra el participante real de Firebase
                    Text(text = uiState.participante.ifBlank { "Participante" }, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = "Persona vinculada", color = Color.Gray, fontSize = 12.sp)
                }
                Icon(Icons.Default.Add, null, tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Repetir",
                placeholder = "Nunca",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.repetir,
                onValorChange = { viewModel.onRepetirChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Recordar antes de:",
                placeholder = "Sin aviso",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.recordar,
                onValorChange = { viewModel.onRecordarChange(it) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

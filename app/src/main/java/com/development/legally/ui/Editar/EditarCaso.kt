package com.development.legally.ui.Editar

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.EdicionSuprema
import com.development.legally.ui.cases.CasosViewModel

@Composable
fun EditarCasoScreen(
    caseId: String?,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    viewModel: CasosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Opciones para los desplegables
    val tiposProceso = listOf("Penal", "Civil", "Laboral", "Familia", "Administrativo", "Otro")
    val estadosCaso = listOf("Activo", "En proceso", "Pendiente", "Finalizado", "Archivado")

    // Cargar datos del caso y clientes al entrar
    LaunchedEffect(caseId) {
        viewModel.setCaseForEditing(caseId)
    }

    // Navegar atrás al guardar con éxito
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSave()
            viewModel.resetSaveState()
        }
    }

    EdicionSuprema.PantallaBase(
        titulo = "Editar Caso",
        textoBotonGuardar = if (uiState.isSaving) "Guardando..." else "Guardar cambios",
        textoBotonEliminar = "Eliminar caso",
        onAtras = onBack,
        onCancelar = onBack,
        onGuardar = { viewModel.guardarCaso() },
        onDuplicar = onDuplicate,
        onEliminar = { viewModel.eliminarCaso() }
    ) {
        EdicionSuprema.TituloSeccion(
            titulo = "Información General",
            logo = R.drawable.ic_stat_folder
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Número de expediente",
                placeholder = "Ingrese número...",
                valor = uiState.numeroExpediente,
                onValorChange = { viewModel.onNumeroExpedienteChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Título del caso",
                placeholder = "Ingrese título...",
                valor = uiState.tituloCaso,
                onValorChange = { viewModel.onTituloCasoChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Tipo de proceso",
                placeholder = "Seleccione...",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.tipoProceso,
                options = tiposProceso,
                onValorChange = { viewModel.onTipoProcesoChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Estado del caso",
                placeholder = "Seleccione...",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.estadoCaso,
                options = estadosCaso,
                onValorChange = { viewModel.onEstadoCasoChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Descripción del caso",
            placeholder = "Ingrese descripción...",
            valor = uiState.descripcion,
            onValorChange = { viewModel.onDescripcionChange(it) },
            height = 150.dp,
            maxChars = 1000
        )

        Spacer(modifier = Modifier.height(8.dp))

        EdicionSuprema.TituloSeccion(
            titulo = "Cliente vinculado",
            logo = R.drawable.ic_nav_clientes_off
        )

        // Selector de cliente unificado
        EdicionSuprema.ElementoEdicion(
            titulo = "Seleccionar Cliente",
            placeholder = "Haga clic para seleccionar...",
            tipo = EdicionSuprema.TipoDato.LISTA,
            valor = uiState.clientName,
            options = uiState.availableClients.map { "${it.name} ${it.lastName}" },
            onValorChange = { viewModel.onClientNameChange(it) }
        )
    }
}

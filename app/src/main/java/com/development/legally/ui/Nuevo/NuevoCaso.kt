package com.development.legally.ui.Nuevo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.BaseFormScreen
import com.development.legally.ui.ClasesSupremas.FormDataType
import com.development.legally.ui.ClasesSupremas.FormElement
import com.development.legally.ui.ClasesSupremas.FormSectionHeader
import com.development.legally.ui.cases.CasosViewModel

@Composable
fun NuevoCasoScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: CasosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Opciones para los desplegables
    val tiposProceso = listOf("Penal", "Civil", "Laboral", "Familia", "Administrativo", "Otro")
    val estadosCaso = listOf("Activo", "En proceso", "Pendiente", "Finalizado", "Archivado")
    val prioridades = listOf("Baja", "Media", "Alta", "Urgente")

    LaunchedEffect(Unit) {
        viewModel.setCaseForEditing("new")
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSave()
            viewModel.resetSaveState()
        }
    }

    BaseFormScreen(
        title = "Nuevo Expediente",
        saveButtonLabel = if (uiState.isSaving) "Guardando..." else "Guardar Expediente",
        onBackClick = onBack,
        onCancelConfirm = onBack,
        onSaveClick = { viewModel.guardarCaso() }
    ) {
        // SECCIÓN: INFORMACIÓN GENERAL
        FormSectionHeader(
            title = "Información General",
            icon = { Icon(painterResource(id = R.drawable.ic_stat_folder), contentDescription = null, tint = Color(0xFF9E8D44)) }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Número de expediente *",
                placeholder = "25-00000-033-PE",
                type = FormDataType.STRING,
                value = uiState.numeroExpediente,
                onValueChange = { viewModel.onNumeroExpedienteChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Título del caso",
                placeholder = "Ingrese título...",
                type = FormDataType.STRING,
                value = uiState.tituloCaso,
                onValueChange = { viewModel.onTituloCasoChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Tipo de proceso",
                placeholder = "Seleccionar",
                type = FormDataType.LIST,
                value = uiState.tipoProceso,
                options = tiposProceso,
                onValueChange = { viewModel.onTipoProcesoChange(it) },
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Estado del caso",
                placeholder = "Seleccionar",
                type = FormDataType.LIST,
                value = uiState.estadoCaso,
                options = estadosCaso,
                onValueChange = { viewModel.onEstadoCasoChange(it) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Agregada Prioridad en Nuevo Caso
        FormElement(
            label = "Prioridad del caso",
            placeholder = "Seleccionar prioridad...",
            type = FormDataType.LIST,
            value = uiState.prioridad,
            options = prioridades,
            onValueChange = { viewModel.onPrioridadChange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormElement(
            label = "Descripción del caso",
            placeholder = "Ingrese descripción...",
            type = FormDataType.STRING,
            value = uiState.descripcion,
            onValueChange = { viewModel.onDescripcionChange(it) },
            height = 150.dp,
            maxChars = 1000
        )

        // SECCIÓN: CLIENTE
        FormSectionHeader(
            title = "Cliente",
            icon = { Icon(painterResource(id = R.drawable.ic_nav_clientes_off), contentDescription = null, tint = Color.White) }
        )

        FormElement(
            label = "Vincular cliente",
            placeholder = "Seleccionar cliente...",
            type = FormDataType.LIST,
            value = uiState.clientName,
            options = uiState.availableClients.map { "${it.name} ${it.lastName}" },
            onValueChange = { viewModel.onClientNameChange(it) }
        )
    }
}

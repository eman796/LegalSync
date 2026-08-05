package com.development.legally.ui.Nuevo

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.Case
import com.development.legally.data.repository.CaseRepository
import com.development.legally.ui.ClasesSupremas.BaseFormScreen
import com.development.legally.ui.ClasesSupremas.FormDataType
import com.development.legally.ui.ClasesSupremas.FormElement
import com.development.legally.ui.ClasesSupremas.FormSectionHeader
import com.development.legally.ui.theme.LegallyTheme
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NuevoCasoUiState(
    val numeroExpediente: String = "",
    val tituloCaso: String = "",
    val tipoProceso: String = "",
    val estadoCaso: String = "",
    val descripcion: String = "",
    val clienteSeleccionado: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)

// 2. ViewModel integrado
class NuevoCasoViewModel(private val repository: CaseRepository = CaseRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(NuevoCasoUiState())
    val uiState: StateFlow<NuevoCasoUiState> = _uiState.asStateFlow()

    fun onNumeroExpedienteChange(value: String) {
        _uiState.update { it.copy(numeroExpediente = value) }
    }

    fun onTituloCasoChange(value: String) {
        _uiState.update { it.copy(tituloCaso = value) }
    }

    fun onTipoProcesoChange(value: String) {
        _uiState.update { it.copy(tipoProceso = value) }
    }

    fun onEstadoCasoChange(value: String) {
        _uiState.update { it.copy(estadoCaso = value) }
    }

    fun onDescripcionChange(value: String) {
        _uiState.update { it.copy(descripcion = value) }
    }

    fun onClienteSeleccionadoChange(value: String) {
        _uiState.update { it.copy(clienteSeleccionado = value) }
    }

    fun guardarCaso() {
        val state = _uiState.value
        if (state.numeroExpediente.isBlank()) return

        _uiState.update { it.copy(isSaving = true, error = null) }

        val nuevoCaso = Case(
            caseNumber = state.numeroExpediente,
            CaseTittle = state.tituloCaso,
            processType = state.tipoProceso,
            status = state.estadoCaso,
            description = state.descripcion,
            clientName = state.clienteSeleccionado,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )

        viewModelScope.launch {
            val result = repository.createCase(nuevoCaso)
            if (result.isSuccess) {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                Log.e("NuevoCaso", "Error al crear: $errorMsg")
                _uiState.update { it.copy(isSaving = false, error = errorMsg) }
            }
        }
    }
}

@Composable
fun NuevoCasoScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: NuevoCasoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSave()
        }
    }

    NuevoCasoContent(
        uiState = uiState,
        onBack = onBack,
        onSaveClick = { viewModel.guardarCaso() },
        onNumeroExpedienteChange = { viewModel.onNumeroExpedienteChange(it) },
        onTituloCasoChange = { viewModel.onTituloCasoChange(it) },
        onTipoProcesoChange = { viewModel.onTipoProcesoChange(it) },
        onEstadoCasoChange = { viewModel.onEstadoCasoChange(it) },
        onDescripcionChange = { viewModel.onDescripcionChange(it) },
        onClienteSeleccionadoChange = { viewModel.onClienteSeleccionadoChange(it) }
    )
}

@Composable
fun NuevoCasoContent(
    uiState: NuevoCasoUiState,
    onBack: () -> Unit,
    onSaveClick: () -> Unit,
    onNumeroExpedienteChange: (String) -> Unit,
    onTituloCasoChange: (String) -> Unit,
    onTipoProcesoChange: (String) -> Unit,
    onEstadoCasoChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onClienteSeleccionadoChange: (String) -> Unit
) {
    BaseFormScreen(
        title = "Nuevo Caso",
        saveButtonLabel = if (uiState.isSaving) "Guardando..." else "Guardar Caso",
        onBackClick = onBack,
        onCancelConfirm = onBack,
        onSaveClick = onSaveClick
    ) {
        // SECCIÓN: INFORMACIÓN GENERAL
        FormSectionHeader(
            title = "Información General",
            icon = { Icon(painterResource(id = R.drawable.ic_stat_folder), contentDescription = null, tint = Color(0xFF9E8D44)) }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Número de expediente",
                placeholder = "25-00000-033-PE",
                type = FormDataType.STRING,
                value = uiState.numeroExpediente,
                onValueChange = onNumeroExpedienteChange,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Título del caso",
                placeholder = "Ingrese título...",
                type = FormDataType.STRING,
                value = uiState.tituloCaso,
                onValueChange = onTituloCasoChange,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Tipo de proceso",
                placeholder = "Penal",
                type = FormDataType.LIST,
                value = uiState.tipoProceso,
                onValueChange = onTipoProcesoChange,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Estado del caso",
                placeholder = "Activo",
                type = FormDataType.LIST,
                value = uiState.estadoCaso,
                onValueChange = onEstadoCasoChange,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FormElement(
            label = "Descripción del caso",
            placeholder = "Ingrese descripción...",
            type = FormDataType.STRING,
            value = uiState.descripcion,
            onValueChange = onDescripcionChange,
            height = 150.dp,
            maxChars = 1000
        )

        // SECCIÓN: CLIENTE
        FormSectionHeader(
            title = "Cliente",
            icon = { Icon(painterResource(id = R.drawable.ic_nav_clientes_off), contentDescription = null, tint = Color.White) }
        )

        FormElement(
            label = "Seleccionar cliente",
            placeholder = "Emanuel Calvo",
            type = FormDataType.LIST,
            value = uiState.clienteSeleccionado,
            onValueChange = onClienteSeleccionadoChange,
            onClick = { /* TODO: Implementar selección */ }
        )
    }
}

@Preview
@Composable
fun NuevoCasoPreview() {
    LegallyTheme {
        NuevoCasoContent(
            uiState = NuevoCasoUiState(),
            onBack = {},
            onSaveClick = {},
            onNumeroExpedienteChange = {},
            onTituloCasoChange = {},
            onTipoProcesoChange = {},
            onEstadoCasoChange = {},
            onDescripcionChange = {},
            onClienteSeleccionadoChange = {}
        )
    }
}


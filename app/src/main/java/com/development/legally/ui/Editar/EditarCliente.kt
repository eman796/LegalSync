package com.development.legally.ui.Editar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.Client
import com.development.legally.data.repository.ClientRepository
import com.development.legally.ui.ClasesSupremas.EdicionSuprema
import com.development.legally.ui.theme.LegallyTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditarClienteUiState(
    val nombre: String = "",
    val tipoPersona: String = "Física",
    val identidad: String = "Cédula",
    val fechaNacimiento: String = "",
    val nacionalidad: String = "",
    val correo: String = "",
    val telefono: String = "",
    val numeroDocumento: String = "",
    val descripcion: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

class EditarClienteViewModel(private val repository: ClientRepository = ClientRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(EditarClienteUiState())
    val uiState: StateFlow<EditarClienteUiState> = _uiState

    fun loadClient(clientId: String?) {
        if (clientId == null) return
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getClientById(clientId).onSuccess { client ->
                _uiState.update { it.copy(
                    nombre = client.name,
                    tipoPersona = client.personType,
                    correo = client.email,
                    telefono = client.phone,
                    numeroDocumento = client.id,
                    descripcion = client.description,
                    isLoading = false
                ) }
            }
        }
    }

    fun onNombreChange(v: String) = _uiState.update { it.copy(nombre = v) }
    fun onCorreoChange(v: String) = _uiState.update { it.copy(correo = v) }
    fun onTelefonoChange(v: String) = _uiState.update { it.copy(telefono = v) }
    fun onDocChange(v: String) = _uiState.update { it.copy(numeroDocumento = v) }
    fun onDescChange(v: String) = _uiState.update { it.copy(descripcion = v) }

    fun guardar() {
        // Lógica de guardado...
        _uiState.update { it.copy(isSaved = true) }
    }
}

@Composable
fun EditarClienteScreen(
    clientId: String?,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    viewModel: EditarClienteViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(clientId) {
        viewModel.loadClient(clientId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSave()
    }

    EditarClienteContent(
        uiState = uiState,
        onBack = onBack,
        onSave = { viewModel.guardar() },
        onDelete = onDelete,
        onDuplicate = onDuplicate,
        onNombreChange = { viewModel.onNombreChange(it) },
        onCorreoChange = { viewModel.onCorreoChange(it) },
        onTelefonoChange = { viewModel.onTelefonoChange(it) },
        onDocChange = { viewModel.onDocChange(it) },
        onDescChange = { viewModel.onDescChange(it) }
    )
}

@Composable
fun EditarClienteContent(
    uiState: EditarClienteUiState,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    onNombreChange: (String) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onDocChange: (String) -> Unit,
    onDescChange: (String) -> Unit
) {
    EdicionSuprema.PantallaBase(
        titulo = "Editar Cliente",
        textoBotonGuardar = "Guardar Cliente",
        textoBotonEliminar = "Eliminar cliente",
        onAtras = onBack,
        onCancelar = onBack,
        onGuardar = onSave,
        onDuplicar = onDuplicate,
        onEliminar = onDelete
    ) {
        EdicionSuprema.TituloSeccion(titulo = "Información General", logo = R.drawable.ic_stat_folder)

        EdicionSuprema.ElementoEdicion(
            titulo = "Nombre del cliente",
            valor = uiState.nombre,
            onValorChange = onNombreChange,
            placeholder = ""
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Tipo de persona",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.tipoPersona,
                onValorChange = { },
                modifier = Modifier.weight(1f),
                placeholder =""
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Identidad",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.identidad,
                onValorChange = { },
                modifier = Modifier.weight(1f),
                placeholder = ""
            )
        }

        EdicionSuprema.TituloSeccion(titulo = "Contacto")

        EdicionSuprema.ElementoEdicion(
            titulo = "Correo",
            valor = uiState.correo,
            onValorChange = onCorreoChange,
            placeholder = "",
            leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.Gray)}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Teléfono",
                valor = uiState.telefono,
                onValorChange = onTelefonoChange,
                modifier = Modifier.weight(1.2f),
                placeholder = "Teléfono",
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = Color.Gray) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Numero de documento",
                valor = uiState.numeroDocumento,
                onValorChange = onDocChange,
                modifier = Modifier.weight(1f),
                placeholder = "Número de documento",
            )
        }

        EdicionSuprema.TituloSeccion(titulo = "Descripción del cliente")

        EdicionSuprema.ElementoEdicion(
            titulo = "",
            valor = uiState.descripcion,
            onValorChange = onDescChange,
            height = 150.dp,
            maxChars = 1000,
            placeholder = "Descripcion"
        )
    }
}

package com.development.legally.ui.Editar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.Client
import com.development.legally.data.repository.ClientRepository
import com.development.legally.ui.ClasesSupremas.EdicionSuprema
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.clients.ClientViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
fun EditarClienteScreen(
    clientId: String?,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    viewModel: ClientViewModel = viewModel() // Usamos el ViewModel de clientes que ya tiene la lógica de guardado y conteo
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Variables locales para el formulario de edición
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var personType by remember { mutableStateOf("Física") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var documentNumber by remember { mutableStateOf("") }

    LaunchedEffect(clientId) {
        if (clientId != null && clientId != "new") {
            viewModel.loadClientById(clientId) { client ->
                client?.let {
                    name = it.name
                    lastName = it.lastName
                    personType = it.personType
                    email = it.email
                    phone = it.phone
                    address = it.address
                    description = it.description
                    documentNumber = it.documentNumber
                }
            }
        }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.resetSaveState()
            onSave()
        }
    }

    EdicionSuprema.PantallaBase(
        titulo = "Editar Cliente",
        textoBotonGuardar = if (uiState.isSaving) "Guardando..." else "Guardar cambios",
        textoBotonEliminar = "Eliminar cliente",
        onAtras = onBack,
        onCancelar = onBack,
        onGuardar = {
            val updatedClient = Client(
                id = clientId ?: "",
                name = name,
                lastName = lastName,
                personType = personType,
                email = email,
                phone = phone,
                address = address,
                description = description,
                documentNumber = documentNumber
            )
            viewModel.updateClient(updatedClient) { _, _ -> }
        },
        onDuplicar = {
            val currentClient = Client(
                id = clientId ?: "",
                name = name,
                lastName = lastName,
                personType = personType,
                email = email,
                phone = phone,
                address = address,
                description = description,
                documentNumber = documentNumber
            )
            viewModel.duplicarClient(currentClient) { _, _ -> }
        },
        onEliminar = {
            if (clientId != null) {
                viewModel.deleteClient(clientId) { success, _ -> if (success) onDelete() }
            }
        }
    ) {
        EdicionSuprema.TituloSeccion(titulo = "Información General", logo = R.drawable.ic_stat_folder)

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Nombre *",
                valor = name,
                onValorChange = { name = it },
                modifier = Modifier.weight(1f),
                placeholder = "Ingrese nombre..."
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Apellido *",
                valor = lastName,
                onValorChange = { lastName = it },
                modifier = Modifier.weight(1f),
                placeholder = "Ingrese apellido..."
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Tipo de persona",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = personType,
                options = listOf("Física", "Jurídica"),
                onValorChange = { personType = it },
                modifier = Modifier.weight(1f),
                placeholder = "Seleccione..."
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Identificación",
                valor = documentNumber,
                onValorChange = { documentNumber = it },
                modifier = Modifier.weight(1f),
                placeholder = "Número..."
            )
        }

        EdicionSuprema.TituloSeccion(titulo = "Contacto", logo = R.drawable.ic_launcher_foreground)

        EdicionSuprema.ElementoEdicion(
            titulo = "Correo",
            valor = email,
            onValorChange = { email = it },
            placeholder = "ejemplo@correo.com",
            leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.Gray) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Teléfono",
            valor = phone,
            onValorChange = { phone = it },
            placeholder = "+506 0000 0000",
            leadingIcon = { Icon(Icons.Default.Phone, null, tint = Color.Gray) }
        )

        EdicionSuprema.TituloSeccion(titulo = "Descripción y Dirección")

        EdicionSuprema.ElementoEdicion(
            titulo = "Dirección física",
            valor = address,
            onValorChange = { address = it },
            placeholder = "Ubicación del cliente..."
        )

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Notas del cliente",
            valor = description,
            onValorChange = { description = it },
            height = 150.dp,
            maxChars = 1000,
            placeholder = "Ingrese detalles..."
        )
    }
}

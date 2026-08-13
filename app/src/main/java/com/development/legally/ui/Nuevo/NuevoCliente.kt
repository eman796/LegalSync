package com.development.legally.ui.Nuevo

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.Client
import com.development.legally.ui.ClasesSupremas.BaseFormScreen
import com.development.legally.ui.ClasesSupremas.FormDataType
import com.development.legally.ui.ClasesSupremas.FormElement
import com.development.legally.ui.ClasesSupremas.FormSectionHeader
import com.development.legally.ui.clients.ClientViewModel

@Composable
fun NuevoClienteScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: ClientViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var tipoPersona by remember { mutableStateOf("Física") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var identificacion by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    // Observar si se guardó con éxito para cerrar la pantalla
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.resetSaveState()
            onSave()
        }
    }

    BaseFormScreen(
        title = "Nuevo Cliente",
        saveButtonLabel = if (uiState.isSaving) "Guardando..." else "Guardar Cliente",
        onBackClick = onBack,
        onCancelConfirm = onBack,
        onSaveClick = {
            if (nombre.isNotBlank() && apellido.isNotBlank()) {
                val newClient = Client(
                    name = nombre,
                    lastName = apellido,
                    personType = tipoPersona,
                    birthDate = fechaNacimiento,
                    nationality = nacionalidad,
                    email = correo,
                    phone = telefono,
                    documentNumber = identificacion,
                    address = address,
                    description = descripcion
                )
                viewModel.createClient(newClient) { _, _ -> }
            }
        }
    ) {
        FormSectionHeader(
            title = "Información General",
            icon = { Icon(painterResource(id = R.drawable.ic_stat_folder), contentDescription = null, tint = Color(0xFF9E8D44)) }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Nombre *",
                placeholder = "Nombre...",
                type = FormDataType.STRING,
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Apellido *",
                placeholder = "Apellido...",
                type = FormDataType.STRING,
                value = apellido,
                onValueChange = { apellido = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Tipo de persona",
                placeholder = "Física",
                type = FormDataType.LIST,
                value = tipoPersona,
                options = listOf("Física", "Jurídica"),
                onValueChange = { tipoPersona = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Nacionalidad",
                placeholder = "Seleccione...",
                type = FormDataType.LIST,
                value = nacionalidad,
                options = listOf("Costa Rica", "Nicaragua", "Panamá", "Estados Unidos", "Otra"),
                onValueChange = { nacionalidad = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FormElement(
            label = "Cédula / Documento",
            placeholder = "0-0000-0000",
            type = FormDataType.STRING,
            value = identificacion,
            onValueChange = { identificacion = it }
        )

        FormSectionHeader(title = "Contacto")

        FormElement(
            label = "Correo electrónico",
            placeholder = "ejemplo@correo.com",
            type = FormDataType.STRING,
            value = correo,
            onValueChange = { correo = it },
            leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormElement(
            label = "Teléfono",
            placeholder = "+506 0000 0000",
            type = FormDataType.STRING,
            value = telefono,
            onValueChange = { telefono = it },
            leadingIcon = { Icon(Icons.Default.Phone, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) }
        )

        FormSectionHeader(title = "Descripción y Dirección")

        FormElement(
            label = "Dirección física",
            placeholder = "Ingrese dirección...",
            type = FormDataType.STRING,
            value = address,
            onValueChange = { address = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        FormElement(
            label = "Notas",
            placeholder = "Detalles adicionales...",
            type = FormDataType.STRING,
            value = descripcion,
            onValueChange = { descripcion = it },
            height = 120.dp,
            maxChars = 1000
        )
    }
}

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.BaseFormScreen
import com.development.legally.ui.ClasesSupremas.FormDataType
import com.development.legally.ui.ClasesSupremas.FormElement
import com.development.legally.ui.ClasesSupremas.FormSectionHeader
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun NuevoClienteScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var tipoPersona by remember { mutableStateOf("") }
    var identidad by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var numeroDocumento by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    BaseFormScreen(
        title = "Nuevo Cliente",
        saveButtonLabel = "Guardar Cliente",
        onBackClick = onBack,
        onCancelConfirm = onBack,
        onSaveClick = onSave
    ) {
        // SECCIÓN 1: INFORMACIÓN GENERAL
        FormSectionHeader(
            title = "Información General",
            icon = { Icon(painterResource(id = R.drawable.ic_stat_folder), contentDescription = null, tint = Color(0xFF9E8D44)) }
        )

        FormElement(
            label = "Nombre del cliente",
            placeholder = "Progra V grupo 3",
            type = FormDataType.STRING,
            value = nombre,
            onValueChange = { nombre = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Tipo de persona",
                placeholder = "Física",
                type = FormDataType.LIST,
                value = tipoPersona,
                onValueChange = { tipoPersona = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Identidad",
                placeholder = "Cédula",
                type = FormDataType.LIST,
                value = identidad,
                onValueChange = { identidad = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Fecha de nacimiento",
                placeholder = "05/06/2025 XX:XX",
                type = FormDataType.LIST,
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Nacionalidad",
                placeholder = "Costarricense",
                type = FormDataType.LIST,
                value = nacionalidad,
                onValueChange = { nacionalidad = it },
                modifier = Modifier.weight(1f)
            )
        }

        // SECCIÓN 2: CONTACTO
        FormSectionHeader(title = "Contacto")

        FormElement(
            label = "Correo",
            placeholder = "example@gmail.com",
            type = FormDataType.STRING,
            value = correo,
            onValueChange = { correo = it },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Teléfono",
                placeholder = "+506 6282 1116",
                type = FormDataType.STRING,
                value = telefono,
                onValueChange = { telefono = it },
                modifier = Modifier.weight(1.2f),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp)) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Numero de documento",
                placeholder = "5-0456-0691",
                type = FormDataType.STRING,
                value = numeroDocumento,
                onValueChange = { numeroDocumento = it },
                modifier = Modifier.weight(1f)
            )
        }

        // SECCIÓN 3: DESCRIPCIÓN
        FormSectionHeader(title = "Descripción del cliente")

        FormElement(
            label = "",
            placeholder = "Escriba aquí...",
            type = FormDataType.STRING,
            value = descripcion,
            onValueChange = { descripcion = it },
            height = 150.dp,
            maxChars = 1000
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NuevoClientePreview() {
    LegallyTheme {
        NuevoClienteScreen(onBack = {}, onSave = {})
    }
}

package com.development.legally.ui.Nuevo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.*

@Composable
fun NuevoEventoScreen(onBack: () -> Unit, onSave: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    BaseFormScreen(title = "Nuevo Evento", saveButtonLabel = "Guardar Evento", onBackClick = onBack, onCancelConfirm = onBack, onSaveClick = onSave) {
        FormSectionHeader(title = "Detalles del Evento", icon = { Icon(painterResource(id = R.drawable.ic_stat_folder), null, tint = Color(0xFF9E8D44)) })
        FormElement("Título del Evento", "Reunión de prueba", FormDataType.STRING, titulo, { titulo = it })
        Spacer(Modifier.height(16.dp))
        FormElement("Tipo de evento", "Seleccione...", FormDataType.LIST, tipo, { tipo = it })
        Spacer(Modifier.height(16.dp))
        FormElement("Lugar", "Ubicación...", FormDataType.LIST, lugar, { lugar = it }, leadingIcon = { Icon(painterResource(R.drawable.ic_location_pin), null, tint = Color.White) })
        FormSectionHeader(title = "Descripción")
        FormElement("", "Detalles del evento...", FormDataType.STRING, descripcion, { descripcion = it }, height = 120.dp, maxChars = 500)
    }
}
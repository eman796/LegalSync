package com.development.legally.ui.Nuevo

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
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.*
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun NuevoEventoScreen(onBack: () -> Unit, onSave: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var fechaHora by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var casoRelacionado by remember { mutableStateOf("") }
    var repetir by remember { mutableStateOf("") }
    var recordar by remember { mutableStateOf("") }

    BaseFormScreen(
        title = "Nuevo evento", 
        saveButtonLabel = "Guardar evento", 
        onBackClick = onBack, 
        onCancelConfirm = onBack, 
        onSaveClick = onSave
    ) {
        FormSectionHeader(
            title = "Información General", 
            icon = { Icon(painterResource(id = R.drawable.ic_expedientes_edit), null, tint = Color(0xFF9E8D44)) }
        )

        FormElement("Título del Evento", "25-00000-033-PE", FormDataType.STRING, titulo, { titulo = it })

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            FormElement("Tipo de evento", "ALTÍSIMA", FormDataType.LIST, tipo, { tipo = it }, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            FormElement("Estado", "Ocupada", FormDataType.LIST, estado, { estado = it }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            FormElement("Fecha y hora", "05/06/2025 XX:XX", FormDataType.LIST, fechaHora, { fechaHora = it }, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            FormElement("Duración", "05/06/2025 XX:XX", FormDataType.LIST, duracion, { duracion = it }, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        FormElement(
            label = "Lugar del evento", 
            placeholder = "Penal", 
            type = FormDataType.LIST, 
            value = lugar, 
            onValueChange = { lugar = it }
        )

        Spacer(Modifier.height(16.dp))

        FormElement(
            label = "Descripción del evento", 
            placeholder = "05/06/2025", 
            type = FormDataType.STRING, 
            value = descripcion, 
            onValueChange = { descripcion = it }, 
            height = 120.dp, 
            maxChars = 500
        )

        Spacer(Modifier.height(16.dp))

        FormElement("Caso relacionado", "25-000044-033-PE - Peculado", FormDataType.LIST, casoRelacionado, { casoRelacionado = it })

        FormSectionHeader(title = "Quienes participan")
        
        // Tarjeta de participante como en la imagen
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
                    Text("Emanuel Calvo", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Cédula: 5-0456-0691", color = Color.Gray, fontSize = 12.sp)
                }
                Icon(Icons.Default.Add, null, tint = Color.Gray, modifier = Modifier.size(32.dp))
            }
        }

        FormSectionHeader(title = "Repeticiones")

        Row(Modifier.fillMaxWidth()) {
            FormElement("Repetir", "Ocupada", FormDataType.LIST, repetir, { repetir = it }, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            FormElement("Recordar antes de:", "24 horas", FormDataType.LIST, recordar, { recordar = it }, modifier = Modifier.weight(1f))
        }
        
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun NuevoEventoScreenPreview() {
    LegallyTheme {
        NuevoEventoScreen(onBack = {}, onSave = {})
    }
}

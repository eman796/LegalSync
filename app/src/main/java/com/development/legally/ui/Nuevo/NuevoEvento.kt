package com.development.legally.ui.Nuevo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.google.firebase.firestore.FirebaseFirestore

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

    // Listas de opciones que ahora se pasan directamente al FormElement
    val tiposOptions = listOf("Audiencia", "Cita", "Reunión", "Juicio", "Visita", "Otro")
    val estadosOptions = listOf("Pendiente", "En Progreso", "Completado", "Cancelado", "Ocupado")
    val duracionesOptions = listOf("30 min", "1 hora", "2 horas", "Todo el día")
    val lugaresOptions = listOf("Oficina Principal", "Juzgado de San José", "Penal La Reforma", "Virtual", "Otro")
    val casosOptions = listOf("25-000044-033-PE - Peculado", "En desarrollo...")
    val repetirOptions = listOf("Nunca", "Diariamente", "Semanalmente", "Mensualmente")
    val recordarOptions = listOf("Sin aviso", "15 min antes", "30 min antes", "1 hora antes", "24 horas antes")

    BaseFormScreen(
        title = "Nuevo evento",
        saveButtonLabel = "Guardar evento",
        onBackClick = onBack,
        onCancelConfirm = onBack,
        onSaveClick = {
            if(titulo.isNotBlank()) {
                val db = FirebaseFirestore.getInstance()
                val DatosEvento = hashMapOf(
                    "titulo" to titulo,
                    "tipo" to tipo,
                    "estado" to estado,
                    "fechaHora" to fechaHora,
                    "duracion" to duracion,
                    "lugar" to lugar,
                    "descripcion" to descripcion,
                    "casoRelacionado" to casoRelacionado,
                    "repetir" to repetir,
                    "recordar" to recordar
                )
                db.collection("Agenda").document(titulo + " - " + fechaHora).set(DatosEvento).addOnSuccessListener {
                    onSave()
                }.addOnFailureListener { }
            }
        }
    ) {
        FormSectionHeader(
            title = "Información General",
            icon = { Icon(painterResource(id = R.drawable.ic_expedientes_edit), null, tint = Color(0xFF9E8D44)) }
        )

        FormElement("Título del Evento", "Ej: Audiencia Preliminar", FormDataType.STRING, titulo, { titulo = it })

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            FormElement("Tipo de evento", "Seleccionar", FormDataType.LIST, tipo, { tipo = it }, modifier = Modifier.weight(1f), options = tiposOptions)
            Spacer(Modifier.width(16.dp))
            FormElement("Estado", "Seleccionar", FormDataType.LIST, estado, { estado = it }, modifier = Modifier.weight(1f), options = estadosOptions)
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth()) {
            FormElement("Fecha y hora", "00/00/0000 00:00", FormDataType.DATETIME, fechaHora, { fechaHora = it }, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            FormElement("Duración", "Seleccionar", FormDataType.LIST, duracion, { duracion = it }, modifier = Modifier.weight(1f), options = duracionesOptions)
        }

        Spacer(Modifier.height(16.dp))

        FormElement("Lugar del evento", "Seleccionar lugar", FormDataType.LIST, lugar, { lugar = it }, options = lugaresOptions)

        Spacer(Modifier.height(16.dp))

        FormElement(
            label = "Descripción del evento",
            placeholder = "Detalles adicionales...",
            type = FormDataType.STRING,
            value = descripcion,
            onValueChange = { descripcion = it },
            height = 120.dp,
            maxChars = 500
        )

        Spacer(Modifier.height(16.dp))

        FormElement("Caso relacionado", "Vincular expediente...", FormDataType.LIST, casoRelacionado, { casoRelacionado = it }, options = casosOptions)

        FormSectionHeader(title = "Quienes participan")

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
            FormElement("Repetir", "Nunca", FormDataType.LIST, repetir, { repetir = it }, modifier = Modifier.weight(1f), options = repetirOptions)
            Spacer(Modifier.width(16.dp))
            FormElement("Recordar antes de:", "Sin aviso", FormDataType.LIST, recordar, { recordar = it }, modifier = Modifier.weight(1f), options = recordarOptions)
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

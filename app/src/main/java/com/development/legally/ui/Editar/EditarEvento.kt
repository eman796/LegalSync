package com.development.legally.ui.Editar

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
import com.development.legally.ui.ClasesSupremas.EdicionSuprema
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun EditarEventoScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit
) {
    var titulo by remember { mutableStateOf("25-00000-033-PE") }
    var tipo by remember { mutableStateOf("Reunión") }
    var estado by remember { mutableStateOf("Ocupad@") }
    var fechaHora by remember { mutableStateOf("05/06/2025 XX:XX") }
    var duracion by remember { mutableStateOf("05/06/2025 XX:XX") }
    var lugar by remember { mutableStateOf("HEARTBREAK AVENUE, TWICELAND") }
    var descripcion by remember { mutableStateOf("05/06/2025") }
    var casoRelacionado by remember { mutableStateOf("25-000044-033-PE - Peculado") }

    EdicionSuprema.PantallaBase(
        titulo = "Editar evento",
        textoBotonGuardar = "Guardar evento",
        textoBotonEliminar = "Eliminar caso",
        onAtras = onBack,
        onCancelar = onBack,
        onGuardar = onSave,
        onDuplicar = onDuplicate,
        onEliminar = onDelete
    ) {
        EdicionSuprema.TituloSeccion(
            titulo = "Información General",
            logo = R.drawable.ic_expedientes_edit
        )

        EdicionSuprema.ElementoEdicion(
            titulo = "Título del Evento",
            placeholder = "Ingrese título...",
            valor = titulo,
            onValorChange = { titulo = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Tipo de evento",
                placeholder = "Reunión",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = tipo,
                onValorChange = { tipo = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Estado",
                placeholder = "Ocupad@",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = estado,
                onValorChange = { estado = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Fecha y hora",
                placeholder = "05/06/2025 XX:XX",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = fechaHora,
                onValorChange = { fechaHora = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Duración",
                placeholder = "05/06/2025 XX:XX",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = duracion,
                onValorChange = { duracion = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Lugar del evento",
            placeholder = "Seleccione ubicación...",
            tipo = EdicionSuprema.TipoDato.LISTA,
            valor = lugar,
            onValorChange = { lugar = it },
            leadingIcon = { Icon(painterResource(R.drawable.ic_location_pin), null, tint = Color.White, modifier = Modifier.size(20.dp)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Descripción del evento",
            placeholder = "Ingrese descripción...",
            valor = descripcion,
            onValorChange = { descripcion = it },
            height = 120.dp,
            maxChars = 500
        )

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Caso relacionado",
            placeholder = "Seleccione caso...",
            tipo = EdicionSuprema.TipoDato.LISTA,
            valor = casoRelacionado,
            onValorChange = { casoRelacionado = it }
        )

        EdicionSuprema.TituloSeccion(titulo = "Quienes participan")

        // Tarjeta de participante
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
                Icon(Icons.Default.Add, null, tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Repetir",
                placeholder = "24 horas",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = "24 horas",
                onValorChange = { },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Recordar antes de:",
                placeholder = "24 horas",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = "24 horas",
                onValorChange = { },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditarEventoPreview() {
    LegallyTheme {
        EditarEventoScreen(onBack = {}, onSave = {}, onDelete = {}, onDuplicate = {})
    }
}

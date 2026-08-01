package com.development.legally.ui.Editar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
fun EditarCasoScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit
) {
    var numeroExpediente by remember { mutableStateOf("25-00000-033-PE") }
    var tituloCaso by remember { mutableStateOf("25-00000-033-PE") }
    var tipoProceso by remember { mutableStateOf("Penal") }
    var estadoCaso by remember { mutableStateOf("Activo") }
    var descripcion by remember { mutableStateOf("05/06/2025") }

    EdicionSuprema.PantallaBase(
        titulo = "Editar Caso",
        textoBotonGuardar = "Guardar caso",
        textoBotonEliminar = "Eliminar caso",
        onAtras = onBack,
        onCancelar = onBack,
        onGuardar = onSave,
        onDuplicar = onDuplicate,
        onEliminar = onDelete
    ) {
        EdicionSuprema.TituloSeccion(
            titulo = "Información General",
            logo = R.drawable.ic_stat_folder
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Número de expediente",
                placeholder = "Ingrese...",
                valor = numeroExpediente,
                onValorChange = { numeroExpediente = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Título del caso",
                placeholder = "Ingrese...",
                valor = tituloCaso,
                onValorChange = { tituloCaso = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Tipo de proceso",
                placeholder = "Seleccione...",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = tipoProceso,
                onValorChange = { tipoProceso = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Estado del caso",
                placeholder = "Seleccione...",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = estadoCaso,
                onValorChange = { estadoCaso = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Descripción del caso",
            placeholder = "Ingrese descripción...",
            valor = descripcion,
            onValorChange = { descripcion = it },
            height = 150.dp,
            maxChars = 1000
        )

        Spacer(modifier = Modifier.height(8.dp))

        EdicionSuprema.TituloSeccion(
            titulo = "Cliente",
            logo = R.drawable.ic_nav_clientes_off // Usando el de clientes off como en el mockup
        )

        // Tarjeta de cliente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF171E27), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar simple
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF0D1117), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nav_clientes_off),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Emanuel Calvo",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Cédula: 5-0456-0691",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditarCasoPreview() {
    LegallyTheme {
        EditarCasoScreen(onBack = {}, onSave = {}, onDelete = {}, onDuplicate = {})
    }
}

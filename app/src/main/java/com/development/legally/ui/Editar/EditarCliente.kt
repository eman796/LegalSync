package com.development.legally.ui.Editar

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
import com.development.legally.ui.ClasesSupremas.EdicionSuprema
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun EditarClienteScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit
) {
    var nombre by remember { mutableStateOf("Progra V grupo 3") }
    var tipoPersona by remember { mutableStateOf("Física") }
    var identidad by remember { mutableStateOf("Cédula") }
    var fechaNacimiento by remember { mutableStateOf("05/06/2025 XX:XX") }
    var nacionalidad by remember { mutableStateOf("Costarricense") }
    var correo by remember { mutableStateOf("example@gmail.com") }
    var telefono by remember { mutableStateOf("+506 6282 1116") }
    var numeroDocumento by remember { mutableStateOf("5-0456-0691") }
    var descripcion by remember { mutableStateOf("05/06/2025") }

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
        EdicionSuprema.TituloSeccion(
            titulo = "Información General",
            logo = R.drawable.ic_stat_folder
        )

        EdicionSuprema.ElementoEdicion(
            titulo = "Nombre del cliente",
            placeholder = "Ingrese nombre...",
            valor = nombre,
            onValorChange = { nombre = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Tipo de persona",
                placeholder = "Física",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = tipoPersona,
                onValorChange = { tipoPersona = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Identidad",
                placeholder = "Cédula",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = identidad,
                onValorChange = { identidad = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Fecha de nacimiento",
                placeholder = "05/06/2025 XX:XX",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = fechaNacimiento,
                onValorChange = { fechaNacimiento = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Nacionalidad",
                placeholder = "Costarricense",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = nacionalidad,
                onValorChange = { nacionalidad = it },
                modifier = Modifier.weight(1f)
            )
        }

        EdicionSuprema.TituloSeccion(titulo = "Contacto")

        EdicionSuprema.ElementoEdicion(
            titulo = "Correo",
            placeholder = "example@gmail.com",
            valor = correo,
            onValorChange = { correo = it },
            leadingIcon = { Icon(Icons.Default.Email, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Teléfono",
                placeholder = "+506 6282 1116",
                valor = telefono,
                onValorChange = { telefono = it },
                modifier = Modifier.weight(1.2f),
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Numero de documento",
                placeholder = "5-0456-0691",
                valor = numeroDocumento,
                onValorChange = { numeroDocumento = it },
                modifier = Modifier.weight(1f)
            )
        }

        EdicionSuprema.TituloSeccion(titulo = "Descripción del cliente")

        EdicionSuprema.ElementoEdicion(
            titulo = "",
            placeholder = "05/06/2025",
            valor = descripcion,
            onValorChange = { descripcion = it },
            height = 150.dp,
            maxChars = 1000
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditarClientePreview() {
    LegallyTheme {
        EditarClienteScreen(onBack = {}, onSave = {}, onDelete = {}, onDuplicate = {})
    }
}

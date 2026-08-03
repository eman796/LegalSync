package com.development.legally.ui.Nuevo

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NuevoCasoScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var numeroExpediente by remember { mutableStateOf("") }
    var tituloCaso by remember { mutableStateOf("") }
    var tipoProceso by remember { mutableStateOf("") }
    var estadoCaso by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var clienteSeleccionado by remember { mutableStateOf("") }

    val db = remember { FirebaseFirestore.getInstance() }

    BaseFormScreen(
        title = "Nuevo Caso",
        saveButtonLabel = "Guardar Caso",
        onBackClick = onBack,
        onCancelConfirm = onBack,
        onSaveClick = {
            if (numeroExpediente.isNotBlank()) {
                val casoData = hashMapOf(
                    "numeroExpediente" to numeroExpediente,
                    "tituloCaso" to tituloCaso,
                    "tipoProceso" to tipoProceso,
                    "estadoCaso" to estadoCaso,
                    "descripcion" to descripcion,
                    "cliente" to clienteSeleccionado
                )

                db.collection("Expedientes")
                    .document(numeroExpediente + " - " + tituloCaso)
                    .set(casoData)
                    .addOnSuccessListener {
                        onSave() // Llama al callback para navegar hacia atrás o mostrar éxito
                    }
                    .addOnFailureListener {
                    }
            }
        }
    ) {
        // SECCIÓN: INFORMACIÓN GENERAL
        FormSectionHeader(
            title = "Información General",
            icon = { Icon(painterResource(id = R.drawable.ic_stat_folder), contentDescription = null, tint = Color(0xFF9E8D44)) }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Número de expediente",
                placeholder = "25-00000-033-PE",
                type = FormDataType.STRING,
                value = numeroExpediente,
                onValueChange = { numeroExpediente = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Título del caso",
                placeholder = "Ingrese título...",
                type = FormDataType.STRING,
                value = tituloCaso,
                onValueChange = { tituloCaso = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            FormElement(
                label = "Tipo de proceso",
                placeholder = "Penal",
                type = FormDataType.LIST,
                value = tipoProceso,
                onValueChange = { tipoProceso = it },
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            FormElement(
                label = "Estado del caso",
                placeholder = "Activo",
                type = FormDataType.LIST,
                value = estadoCaso,
                onValueChange = { estadoCaso = it },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        FormElement(
            label = "Descripción del caso",
            placeholder = "Ingrese descripción...",
            type = FormDataType.STRING,
            value = descripcion,
            onValueChange = { descripcion = it },
            height = 150.dp,
            maxChars = 1000
        )

        // SECCIÓN: CLIENTE
        FormSectionHeader(
            title = "Cliente",
            icon = { Icon(painterResource(id = R.drawable.ic_nav_clientes_off), contentDescription = null, tint = Color.White) }
        )

        FormElement(
            label = "Seleccionar cliente",
            placeholder = "Emanuel Calvo",
            type = FormDataType.LIST,
            value = clienteSeleccionado,
            onValueChange = { clienteSeleccionado = it },
            onClick = { /* TODO: Implementar selección */ }
        )
    }
}

@Preview
@Composable
fun NuevoCasoPreview() {
    LegallyTheme {
        NuevoCasoScreen(onBack = {}, onSave = {})
    }
}

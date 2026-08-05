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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.EdicionSuprema
import com.development.legally.ui.cases.CasosViewModel
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun EditarCasoScreen(
    caseId: String?,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    viewModel: CasosViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar los datos del caso real al iniciar
    LaunchedEffect(caseId) {
        viewModel.setCaseForEditing(caseId)
    }

    // Navegar atrás cuando se guarde con éxito
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSave()
            viewModel.resetSaveState()
        }
    }

    EdicionSuprema.PantallaBase(
        titulo = "Editar Caso",
        textoBotonGuardar = if (uiState.isSaving) "Guardando..." else "Guardar caso",
        textoBotonEliminar = "Eliminar caso",
        onAtras = onBack,
        onCancelar = onBack,
        onGuardar = { viewModel.guardarCaso() },
        onDuplicar = onDuplicate,
        onEliminar = { viewModel.eliminarCaso() }
    ) {
        EdicionSuprema.TituloSeccion(
            titulo = "Información General",
            logo = R.drawable.ic_stat_folder
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Número de expediente",
                placeholder = "Ingrese...",
                valor = uiState.numeroExpediente,
                onValorChange = { viewModel.onNumeroExpedienteChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Título del caso",
                placeholder = "Ingrese...",
                valor = uiState.tituloCaso,
                onValorChange = { viewModel.onTituloCasoChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            EdicionSuprema.ElementoEdicion(
                titulo = "Tipo de proceso",
                placeholder = "Seleccione...",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.tipoProceso,
                onValorChange = { viewModel.onTipoProcesoChange(it) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            EdicionSuprema.ElementoEdicion(
                titulo = "Estado del caso",
                placeholder = "Seleccione...",
                tipo = EdicionSuprema.TipoDato.LISTA,
                valor = uiState.estadoCaso,
                onValorChange = { viewModel.onEstadoCasoChange(it) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        EdicionSuprema.ElementoEdicion(
            titulo = "Descripción del caso",
            placeholder = "Ingrese descripción...",
            valor = uiState.descripcion,
            onValorChange = { viewModel.onDescripcionChange(it) },
            height = 150.dp,
            maxChars = 1000
        )

        Spacer(modifier = Modifier.height(8.dp))

        EdicionSuprema.TituloSeccion(
            titulo = "Cliente",
            logo = R.drawable.ic_nav_clientes_off
        )

        // Tarjeta de cliente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF171E27), RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                        text = "Cliente vinculado",
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
        EditarCasoScreen(caseId = "123", onBack = {}, onSave = {}, onDelete = {}, onDuplicate = {})
    }
}

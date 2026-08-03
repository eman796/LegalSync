package com.development.legally.ui.ClasesSupremas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.theme.FigmaBackground
import com.development.legally.ui.theme.FigmaGold
import com.development.legally.ui.theme.LegallyTheme

private val FigmaTextWhite = Color(0xFFFFFFFF)
private val FigmaFieldBackground = Color(0xFF171E27)

// Definición de tipos de datos solicitados
enum class FormDataType { STRING, INTEGER, LIST }

/**
 * PANTALLA BASE SUPREMA
 * Contiene: Flecha, Título dinámico, Botón Cancelar (con aviso) y Botón Guardar fijo.
 */
@Composable
fun BaseFormScreen(
    title: String,
    saveButtonLabel: String,
    onBackClick: () -> Unit,
    onCancelConfirm: () -> Unit,
    onSaveClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    var showCancelDialog by remember { mutableStateOf(false) }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            containerColor = Color(0xFF1C2632),
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("¿Cancelar?") },
            text = { Text("Se perderán todos los datos. ¿Estás seguro?") },
            confirmButton = {
                TextButton(onClick = { 
                    showCancelDialog = false
                    onCancelConfirm() 
                }) {
                    Text("SÍ", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("NO", color = FigmaGold)
                }
            }
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = FigmaBackground) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
            Spacer(modifier = Modifier.height(32.dp))

            // BARRA SUPERIOR (Flecha, Título, Cancelar)
            Row(modifier = Modifier.fillMaxWidth().height(32.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = FigmaGold,
                    modifier = Modifier.size(24.dp).clickable { onBackClick() }
                )
                Text(
                    text = title,
                    color = FigmaTextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Cancelar",
                    color = FigmaGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.clickable { showCancelDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CONTENIDO SCROLLEABLE
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                content()
                Spacer(modifier = Modifier.height(32.dp))
            }

            // BOTÓN GUARDAR FIJO
            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FigmaGold),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_save_edit), contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = saveButtonLabel, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

/**
 * 1. TÍTULO DE SECCIÓN
 * Parámetros: Título (String) y Logo opcional.
 */
@Composable
fun FormSectionHeader(title: String, icon: (@Composable () -> Unit)? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        if (icon != null) { 
            Box(modifier = Modifier.size(24.dp)) { icon() }
            Spacer(modifier = Modifier.width(11.dp)) 
        }
        Text(text = title, color = FigmaTextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

/**
 * 2. ELEMENTO (JTextField Equivalente)
 * Parámetros: Título, Placeholder, Tipo de dato, Posición (Padding/Offset), Dimensiones (W, H).
 */
@Composable
fun FormElement(
    label: String,
    placeholder: String,
    type: FormDataType,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    posX: Dp = 0.dp,
    posY: Dp = 0.dp,
    width: Dp = Dp.Unspecified,
    height: Dp = 50.dp,
    leadingIcon: (@Composable () -> Unit)? = null,
    maxChars: Int? = null,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .padding(start = posX, top = posY)
            .then(if (width != Dp.Unspecified) Modifier.width(width) else Modifier.fillMaxWidth())
    ) {
        Text(text = label, color = FigmaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Black)

        Spacer(modifier = Modifier.height(8.dp))

        if (type == FormDataType.LIST) {
            Box(
                modifier = Modifier.fillMaxWidth().height(height)
                    .background(FigmaFieldBackground, RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .clickable(enabled = onClick != null) { onClick?.invoke() }
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (leadingIcon != null) {
                            leadingIcon()
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(text = if (value.isEmpty()) placeholder else value, color = Color.White.copy(alpha = if(value.isEmpty()) 0.5f else 1f), fontSize = 14.sp)
                    }
                    Icon(painter = painterResource(id = R.drawable.ic_arrow_right_gold), contentDescription = null, tint = FigmaGold, modifier = Modifier.size(16.dp))
                }
            }
        } else {
            Column {
                OutlinedTextField(
                    value = value,
                    onValueChange = { if (maxChars == null || it.length <= maxChars) onValueChange(it) },
                    modifier = Modifier.fillMaxWidth().height(height),
                    placeholder = { Text(text = placeholder, color = Color.Gray.copy(alpha = 0.5f), fontSize = 14.sp) },
                    leadingIcon = leadingIcon,
                    keyboardOptions = KeyboardOptions(keyboardType = if (type == FormDataType.INTEGER) KeyboardType.Number else KeyboardType.Text),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = FigmaFieldBackground,
                        unfocusedContainerColor = FigmaFieldBackground,
                        focusedBorderColor = FigmaGold,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = height < 80.dp
                )
                if (maxChars != null) {
                    Text(
                        text = "Caracteres: ${value.length} de $maxChars",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseFormScreenPreview() {
    LegallyTheme {
        BaseFormScreen(
            title = "Nuevo Supremo",
            saveButtonLabel = "Guardar Cambios",
            onBackClick = {},
            onCancelConfirm = {},
            onSaveClick = {},
            content = {
                FormSectionHeader(title = "Datos del Formulario")
                FormElement(
                    label = "Nombre Completo",
                    placeholder = "Ingrese su nombre",
                    type = FormDataType.STRING,
                    value = "",
                    onValueChange = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                FormElement(
                    label = "Edad",
                    placeholder = "Ej. 25",
                    type = FormDataType.INTEGER,
                    value = "",
                    onValueChange = {}
                )
            }
        )
    }
}

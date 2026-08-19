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
import java.text.SimpleDateFormat
import java.util.*

private val FigmaTextWhite = Color(0xFFFFFFFF)
private val FigmaFieldBackground = Color(0xFF171E27)

enum class FormDataType { STRING, INTEGER, LIST, DATETIME, DATE }

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

            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                content()
                Spacer(modifier = Modifier.height(32.dp))
            }

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

@OptIn(ExperimentalMaterial3Api::class)
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
    onClick: (() -> Unit)? = null,
    options: List<String>? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var tempDate by remember { mutableStateOf("") }

    // 1. Selector de Fecha
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))
                        if (type == FormDataType.DATE) {
                            onValueChange(formatted)
                            showDatePicker = false
                        } else {
                            tempDate = formatted
                            showDatePicker = false
                            showTimePicker = true // Al aceptar fecha, abrimos reloj si es DATETIME
                        }
                    }
                }) {
                    Text(if (type == FormDataType.DATE) "ACEPTAR" else "SIGUIENTE", color = FigmaGold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("CANCELAR", color = Color.Gray)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = Color(0xFF1C2632))
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // 2. Selector de Hora
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val formattedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    onValueChange("$tempDate $formattedTime")
                    showTimePicker = false
                }) {
                    Text("ACEPTAR", color = FigmaGold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("ATRÁS", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF1C2632),
            text = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            clockDialColor = Color(0xFF0D1117),
                            selectorColor = FigmaGold,
                            containerColor = Color(0xFF1C2632),
                            periodSelectorSelectedContainerColor = FigmaGold,
                            timeSelectorSelectedContainerColor = FigmaGold
                        )
                    )
                }
            }
        )
    }

    Column(
        modifier = modifier
            .padding(start = posX, top = posY)
            .then(if (width != Dp.Unspecified) Modifier.width(width) else Modifier.fillMaxWidth())
    ) {
        Text(text = label, color = FigmaTextWhite, fontSize = 13.sp, fontWeight = FontWeight.Black)

        Spacer(modifier = Modifier.height(8.dp))

        if (type == FormDataType.LIST || type == FormDataType.DATETIME || type == FormDataType.DATE) {
            Box(
                modifier = Modifier.fillMaxWidth().height(height)
                    .background(FigmaFieldBackground, RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .clickable { 
                        if (type == FormDataType.DATETIME || type == FormDataType.DATE) {
                            showDatePicker = true
                        } else if (options != null) {
                            expanded = true
                        }
                        onClick?.invoke() 
                    }
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        if (leadingIcon != null) {
                            leadingIcon()
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = if (value.isEmpty()) placeholder else value, 
                            color = Color.White.copy(alpha = if(value.isEmpty()) 0.5f else 1f), 
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    val iconRes = if (type == FormDataType.DATETIME || type == FormDataType.DATE) R.drawable.ic_card_clock_figma else R.drawable.ic_arrow_right_gold
                    Icon(
                        painter = painterResource(id = iconRes), 
                        contentDescription = null, 
                        tint = FigmaGold, 
                        modifier = Modifier.size(16.dp)
                    )
                }

                if (options != null) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(FigmaFieldBackground)
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.White) },
                                onClick = {
                                    onValueChange(option)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        } else {
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
                FormElement(label = "Cita", placeholder = "00/00/0000 00:00", type = FormDataType.DATETIME, value = "", onValueChange = {})
            }
        )
    }
}

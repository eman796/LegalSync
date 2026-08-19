package com.development.legally.ui.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.ui.theme.FigmaBackground
import com.development.legally.ui.theme.FigmaGold
import java.text.SimpleDateFormat
import java.util.*

private val FigmaFieldBackground = Color(0xFF171E27)
private val FigmaTextWhite = Color(0xFFFFFFFF)
private val FigmaRed = Color(0xFFF50505)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    eventId: String?,
    viewModel: AgendaViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(eventId) {
        viewModel.setEventForEditing(eventId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.resetSaveState()
            onBackClick()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis: Long ->
                        val datePart = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))
                        val currentStr = uiState.fechaHora
                        val timePart = if (currentStr.contains(" ")) currentStr.split(" ")[1] else "00:00"
                        viewModel.onFechaHoraChange("$datePart $timePart")
                    }
                    showDatePicker = false
                }) { Text("ACEPTAR", color = FigmaGold) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("CANCELAR") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val timePart = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)
                    val currentStr = uiState.fechaHora
                    val datePart = if (currentStr.contains(" ")) currentStr.split(" ")[0] else SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    viewModel.onFechaHoraChange("$datePart $timePart")
                    showTimePicker = false
                }) { Text("ACEPTAR", color = FigmaGold) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("CANCELAR") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FigmaBackground
    ) {
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = FigmaGold)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Barra Superior
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = FigmaGold,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onBackClick() }
                    )

                    Text(
                        text = if (eventId == "new" || eventId == null) "Nuevo Evento" else "Editar Evento",
                        color = FigmaTextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = FigmaGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .clickable { onCancelClick() }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    label = "Título del Evento *",
                    value = uiState.titulo,
                    onValueChange = { viewModel.onTituloChange(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    label = "Caso Relacionado (Expediente)",
                    value = uiState.casoRelacionado,
                    onValueChange = { viewModel.onCasoRelacionadoChange(it) },
                    isDropdown = true,
                    options = uiState.availableCases.map { it.caseNumber }
                )

                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    label = "Quiénes participan (Clientes)",
                    value = uiState.participante,
                    onValueChange = { viewModel.onParticipanteChange(it) },
                    isDropdown = true,
                    options = uiState.availableClients.map { "${it.name} ${it.lastName}" }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    val dateVal = if (uiState.fechaHora.contains(" ")) uiState.fechaHora.split(" ")[0] else uiState.fechaHora
                    InputField(
                        label = "Fecha",
                        value = dateVal,
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        isDropdown = true,
                        onClick = { showDatePicker = true }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    val timeVal = if (uiState.fechaHora.contains(" ")) uiState.fechaHora.split(" ")[1] else ""
                    InputField(
                        label = "Hora",
                        value = timeVal,
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        isDropdown = true,
                        onClick = { showTimePicker = true }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    InputField(
                        label = "Tipo",
                        value = uiState.tipo,
                        onValueChange = { viewModel.onTipoChange(it) },
                        modifier = Modifier.weight(1f),
                        isDropdown = true,
                        options = listOf("Audiencia", "Reunión", "Tarea")
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    InputField(
                        label = "Duración",
                        value = uiState.duracion,
                        onValueChange = { viewModel.onDuracionChange(it) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    label = "Notas / Descripción",
                    value = uiState.descripcion,
                    onValueChange = { viewModel.onDescripcionChange(it) },
                    fieldHeight = 150.dp,
                    isMultiline = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.guardarEvento() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FigmaGold),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.isSaving
                ) {
                    if (uiState.isSaving) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("GUARDAR CAMBIOS", color = FigmaTextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                if (eventId != "new" && eventId != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { viewModel.eliminarEvento() },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FigmaRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("ELIMINAR EVENTO", color = FigmaRed)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    fieldHeight: androidx.compose.ui.unit.Dp = 50.dp,
    isMultiline: Boolean = false,
    isDropdown: Boolean = false,
    options: List<String>? = null,
    onClick: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            color = FigmaTextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(fieldHeight)
                    .background(FigmaFieldBackground, RoundedCornerShape(12.dp))
                    .border(1.dp, FigmaGold, RoundedCornerShape(12.dp))
                    .clickable(enabled = isDropdown || onClick != null) {
                        if (options != null) expanded = true else onClick?.invoke()
                    }
                    .padding(horizontal = 12.dp),
                verticalAlignment = if (isMultiline) Alignment.Top else Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    readOnly = isDropdown || onClick != null,
                    enabled = !(isDropdown || onClick != null),
                    textStyle = TextStyle(color = FigmaTextWhite, fontSize = 15.sp),
                    cursorBrush = SolidColor(FigmaTextWhite),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = if (isMultiline) Alignment.TopStart else Alignment.CenterStart,
                            modifier = Modifier.padding(vertical = if (isMultiline) 12.dp else 0.dp)
                        ) {
                            innerTextField()
                        }
                    }
                )

                if (isDropdown) {
                    Icon(
                        painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
                        contentDescription = null,
                        tint = FigmaGold,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(90f)
                    )
                }
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
    }
}

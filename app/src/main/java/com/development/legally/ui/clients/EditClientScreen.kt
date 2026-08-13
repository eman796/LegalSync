package com.development.legally.ui.clients

import androidx.compose.foundation.background
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.Client
import com.development.legally.ui.ClasesSupremas.EdicionSuprema
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import java.text.SimpleDateFormat
import java.util.*

private val FigmaBackground = Color(0xFF1C2632)
private val FigmaGold = Color(0xFF9E8D44)
private val FigmaFieldBackground = Color(0xFF171E27)
private val FigmaRed = Color(0xFFF50505)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClientScreen(
    clientId: String?,
    modifier: Modifier = Modifier,
    viewModel: ClientViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToClients: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var personType by remember { mutableStateOf("Física") }
    var nationality by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var documentNumber by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(clientId) {
        if (clientId != null && clientId != "new" && clientId.isNotEmpty()) {
            viewModel.loadClientById(clientId) { client ->
                client?.let {
                    name = it.name
                    lastName = it.lastName
                    personType = it.personType.ifEmpty { "Física" }
                    birthDate = it.birthDate
                    nationality = it.nationality
                    email = it.email
                    phone = it.phone
                    address = it.address
                    description = it.description
                    documentNumber = it.documentNumber
                }
            }
        }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.resetSaveState()
            onNavigateBack()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        birthDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text("ACEPTAR", color = FigmaGold) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("CANCELAR") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    // Usamos PantallaBase de EdicionSuprema para tener el botón duplicar arriba
    EdicionSuprema.PantallaBase(
        titulo = "Información del Cliente",
        textoBotonGuardar = if (uiState.isSaving) "Guardando..." else "Guardar Cambios",
        textoBotonEliminar = "Eliminar Cliente",
        onAtras = onNavigateBack,
        onCancelar = onNavigateBack,
        onGuardar = {
            if (name.isNotBlank() && lastName.isNotBlank()) {
                val client = Client(
                    id = clientId ?: "",
                    name = name,
                    lastName = lastName,
                    personType = personType,
                    birthDate = birthDate,
                    nationality = nationality,
                    email = email,
                    phone = phone,
                    address = address,
                    description = description,
                    documentNumber = documentNumber
                )
                viewModel.updateClient(client) { _, _ -> }
            }
        },
        onDuplicar = {
             val currentClient = Client(
                name = name,
                lastName = lastName,
                personType = personType,
                birthDate = birthDate,
                nationality = nationality,
                email = email,
                phone = phone,
                address = address,
                description = description,
                documentNumber = documentNumber
            )
            viewModel.duplicarClient(currentClient) { _, _ -> }
        },
        onEliminar = {
            clientId?.let { viewModel.deleteClient(it) { success, _ -> if (success) onNavigateBack() } }
        }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            InputField("Nombre *", name, { name = it }, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(12.dp))
            InputField("Apellido *", lastName, { lastName = it }, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = "Tipo de Persona",
                value = personType,
                onValueChange = { personType = it },
                modifier = Modifier.weight(1f),
                isDropdown = true,
                options = listOf("Física", "Jurídica")
            )
            Spacer(modifier = Modifier.width(12.dp))
            InputField(
                label = "Nacionalidad",
                value = nationality,
                onValueChange = { nationality = it },
                modifier = Modifier.weight(1f),
                isDropdown = true,
                options = listOf("Costa Rica", "Nicaragua", "Panamá", "Estados Unidos", "Otra")
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        InputField("Identificación", documentNumber, { documentNumber = it }, Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        InputField("Fecha de Nacimiento", birthDate, {}, Modifier.fillMaxWidth(), isDropdown = true, onClick = { showDatePicker = true })

        Spacer(modifier = Modifier.height(24.dp))
        InputField("Email *", email, { email = it }, leadingIcon = { Icon(painterResource(R.drawable.boton_notificaciones_expedientes), null, tint = Color.DarkGray, modifier = Modifier.size(20.dp)) })
        Spacer(modifier = Modifier.height(12.dp))
        InputField("Teléfono", phone, { phone = it }, leadingIcon = { Icon(painterResource(R.drawable.boton_usuario_expedientes), null, tint = Color.DarkGray, modifier = Modifier.size(20.dp)) })
        Spacer(modifier = Modifier.height(12.dp))
        InputField("Dirección", address, { address = it })
        Spacer(modifier = Modifier.height(12.dp))
        InputField("Descripción", description, { description = it }, isMultiline = true, modifier = Modifier.fillMaxWidth().height(150.dp))
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = { TextButton(onClick = { showErrorDialog = false }) { Text("OK") } }
        )
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isDropdown: Boolean = false,
    options: List<String>? = null,
    isMultiline: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        Text(label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isMultiline) 120.dp else 45.dp)
                    .background(FigmaFieldBackground, RoundedCornerShape(8.dp))
                    .clickable {
                        if (isDropdown && options != null) expanded = true
                        else if (onClick != null) onClick()
                        else focusRequester.requestFocus()
                    }
                    .padding(horizontal = 12.dp, vertical = if (isMultiline) 8.dp else 0.dp),
                verticalAlignment = if (isMultiline) Alignment.Top else Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    Box(modifier = Modifier.padding(top = if (isMultiline) 12.dp else 0.dp)) { leadingIcon() }
                    Spacer(Modifier.width(8.dp))
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    readOnly = isDropdown || onClick != null,
                    textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier.weight(1f).fillMaxHeight().focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = if (isMultiline) Alignment.TopStart else Alignment.CenterStart) { innerTextField() }
                    }
                )
                if (isDropdown) {
                    Icon(
                        painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.CenterVertically).size(16.dp).rotate(90f)
                    )
                }
            }
            if (options != null) {
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(FigmaFieldBackground)) {
                    options.forEach { option ->
                        DropdownMenuItem(text = { Text(option, color = Color.White) }, onClick = { onValueChange(option); expanded = false })
                    }
                }
            }
        }
    }
}

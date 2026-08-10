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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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

    var showDatePicker by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
                }
            }
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

    Scaffold(
        bottomBar = {
            LegallyBottomNavigationBar(
                currentRoute = "clients",
                onInicioClick = onNavigateToHome,
                onExpedientesClick = onNavigateToCases,
                onCrearClick = onNavigateToNewCase,
                onAgendaClick = onNavigateToAgenda,
                onClientesClick = onNavigateToClients
            )
        },
        containerColor = FigmaBackground
    ) { paddingValues ->
        EditClientContent(
            modifier = modifier.padding(paddingValues),
            name = name,
            onNameChange = { name = it },
            lastName = lastName,
            onLastNameChange = { lastName = it },
            personType = personType,
            onPersonTypeChange = { personType = it },
            birthDate = birthDate,
            onBirthDateClick = { showDatePicker = true },
            nationality = nationality,
            onNationalityChange = { nationality = it },
            email = email,
            onEmailChange = { email = it },
            phone = phone,
            onPhoneChange = { phone = it },
            address = address,
            onAddressChange = { address = it },
            description = description,
            onDescriptionChange = { description = it },
            onNavigateBack = onNavigateBack,
            isLoading = isLoading,
            onSaveClick = {
                if (name.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()) {
                    isLoading = true
                    val client = Client(
                        id = if (clientId == "new" || clientId == null) "" else clientId,
                        name = name,
                        lastName = lastName,
                        personType = personType,
                        birthDate = birthDate,
                        nationality = nationality,
                        email = email,
                        phone = phone,
                        address = address,
                        description = description
                    )
                    val callback = { success: Boolean, error: String? ->
                        isLoading = false
                        if (success) onNavigateBack()
                        else { errorMessage = error ?: "Error"; showErrorDialog = true }
                    }
                    if (clientId == null || clientId == "new") viewModel.createClient(client, callback)
                    else viewModel.updateClient(client, callback)
                }
            },
            onDeleteClick = {
                clientId?.let {
                    if (it != "new") {
                        isLoading = true
                        viewModel.deleteClient(it) { s, e ->
                            isLoading = false
                            if (s) onNavigateBack() else { errorMessage = e ?: "Error"; showErrorDialog = true }
                        }
                    }
                }
            }
        )
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
private fun EditClientContent(
    modifier: Modifier,
    name: String, onNameChange: (String) -> Unit,
    lastName: String, onLastNameChange: (String) -> Unit,
    personType: String, onPersonTypeChange: (String) -> Unit,
    birthDate: String, onBirthDateClick: () -> Unit,
    nationality: String, onNationalityChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    phone: String, onPhoneChange: (String) -> Unit,
    address: String, onAddressChange: (String) -> Unit,
    description: String, onDescriptionChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    isLoading: Boolean,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize().imePadding().padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(R.drawable.ic_arrow_back), null, tint = FigmaGold, modifier = Modifier.size(24.dp).clickable { onNavigateBack() })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Información del Cliente", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            InputField("Nombre *", name, onNameChange, Modifier.weight(1f))
            Spacer(modifier = Modifier.width(12.dp))
            InputField("Apellido *", lastName, onLastNameChange, Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = "Tipo de Persona",
                value = personType,
                onValueChange = onPersonTypeChange,
                modifier = Modifier.weight(1f),
                isDropdown = true,
                options = listOf("Física", "Jurídica")
            )
            Spacer(modifier = Modifier.width(12.dp))
            InputField(
                label = "Nacionalidad",
                value = nationality,
                onValueChange = onNationalityChange,
                modifier = Modifier.weight(1f),
                isDropdown = true,
                options = listOf("Costa Rica", "Nicaragua", "Panama", "Estados Unidos", "Otra")
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        InputField("Fecha de Nacimiento", birthDate, {}, Modifier.fillMaxWidth(), isDropdown = true, onClick = onBirthDateClick)

        Spacer(modifier = Modifier.height(24.dp))
        InputField("Email *", email, onEmailChange, leadingIcon = { Icon(painterResource(R.drawable.boton_notificaciones_expedientes), null, tint = Color.DarkGray, modifier = Modifier.size(20.dp)) })
        Spacer(modifier = Modifier.height(12.dp))
        InputField("Teléfono", phone, onPhoneChange, leadingIcon = { Icon(painterResource(R.drawable.boton_usuario_expedientes), null, tint = Color.DarkGray, modifier = Modifier.size(20.dp)) })
        Spacer(modifier = Modifier.height(12.dp))
        InputField("Dirección", address, onAddressChange)
        Spacer(modifier = Modifier.height(12.dp))
        InputField("Descripción", description, onDescriptionChange, isMultiline = true, modifier = Modifier.fillMaxWidth().height(150.dp))

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSaveClick, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = FigmaGold), shape = RoundedCornerShape(24.dp), enabled = !isLoading) {
            Text("Guardar Cliente", color = Color.White)
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = onDeleteClick, modifier = Modifier.fillMaxWidth().height(48.dp), border = androidx.compose.foundation.BorderStroke(1.dp, FigmaRed), shape = RoundedCornerShape(24.dp)) {
            Text("Eliminar Cliente", color = FigmaRed)
        }
        Spacer(modifier = Modifier.height(32.dp))
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
                        if (isDropdown) {
                            if (options != null) expanded = true else onClick?.invoke()
                        }
                    }
                    .padding(horizontal = 12.dp),
                verticalAlignment = if (isMultiline) Alignment.Top else Alignment.CenterVertically
            ) {
                if (leadingIcon != null) { leadingIcon(); Spacer(Modifier.width(8.dp)) }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    readOnly = isDropdown,
                    enabled = !isDropdown,
                    textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField -> innerTextField() }
                )
                if (isDropdown) {
                    Icon(
                        painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp).rotate(90f)
                    )
                }
            }

            if (options != null) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color(0xFF171E27))
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

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.Client
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.navigation.LegallyBottomNavigationBar

private val FigmaBackground = Color(0xFF1C2632)
private val FigmaGold = Color(0xFF9E8D44)
private val FigmaFieldBackground = Color(0xFF171E27)
private val FigmaRed = Color(0xFFF50505)

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

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
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
            onBirthDateChange = { birthDate = it },
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
                if (validateFields(name, lastName, email)) {
                    isLoading = true
                    val client = Client(
                        id = if (clientId == "new" || clientId == null) "" else clientId ?: "",
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
                    if (clientId == null || clientId == "new") {
                        viewModel.createClient(client) { success, error ->
                            isLoading = false
                            if (success) {
                                onNavigateBack()
                            } else {
                                errorMessage = error ?: "Error al crear cliente"
                                showErrorDialog = true
                            }
                        }
                    } else {
                        viewModel.updateClient(client) { success, error ->
                            isLoading = false
                            if (success) {
                                onNavigateBack()
                            } else {
                                errorMessage = error ?: "Error al actualizar cliente"
                                showErrorDialog = true
                            }
                        }
                    }
                } else {
                    errorMessage = "Por favor completa todos los campos obligatorios"
                    showErrorDialog = true
                }
            },
            onDeleteClick = {
                clientId?.let {
                    if (it != "new") {
                        isLoading = true
                        viewModel.deleteClient(it) { success, error ->
                            isLoading = false
                            if (success) {
                                onNavigateBack()
                            } else {
                                errorMessage = error ?: "Error al eliminar cliente"
                                showErrorDialog = true
                            }
                        }
                    }
                }
            }
        )
    }
}

private fun validateFields(name: String, lastName: String, email: String): Boolean {
    return name.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()
}

@Composable
private fun EditClientContent(
    modifier: Modifier = Modifier,
    name: String = "",
    onNameChange: (String) -> Unit = {},
    lastName: String = "",
    onLastNameChange: (String) -> Unit = {},
    personType: String = "Física",
    onPersonTypeChange: (String) -> Unit = {},
    birthDate: String = "",
    onBirthDateChange: (String) -> Unit = {},
    nationality: String = "",
    onNationalityChange: (String) -> Unit = {},
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    phone: String = "",
    onPhoneChange: (String) -> Unit = {},
    address: String = "",
    onAddressChange: (String) -> Unit = {},
    description: String = "",
    onDescriptionChange: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    isLoading: Boolean = false,
    onSaveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Volver",
                tint = FigmaGold,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onNavigateBack() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Información del Cliente",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.boton_expedientes_expedientes),
                contentDescription = null,
                tint = FigmaGold,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Información General",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = "Nombre *",
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            InputField(
                label = "Apellido *",
                value = lastName,
                onValueChange = onLastNameChange,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = "Tipo de Persona",
                value = personType,
                onValueChange = onPersonTypeChange,
                modifier = Modifier.weight(1f),
                isDropdown = true
            )
            Spacer(modifier = Modifier.width(12.dp))
            InputField(
                label = "Nacionalidad",
                value = nationality,
                onValueChange = onNationalityChange,
                modifier = Modifier.weight(1f),
                isDropdown = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Fecha de Nacimiento",
            value = birthDate,
            onValueChange = onBirthDateChange,
            modifier = Modifier.fillMaxWidth(),
            isDropdown = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Información de Contacto",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Email *",
            value = email,
            onValueChange = onEmailChange,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.boton_notificaciones_expedientes),
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = "Teléfono",
                value = phone,
                onValueChange = onPhoneChange,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.boton_usuario_expedientes),
                        contentDescription = null,
                        tint = Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Dirección",
            value = address,
            onValueChange = onAddressChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Descripción Adicional",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Descripción",
            value = description,
            onValueChange = onDescriptionChange,
            isMultiline = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Text(
            text = "${description.length}/1000",
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FigmaGold),
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.boton_crear_expedientes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Guardar Cliente", fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FigmaRed),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = FigmaRed),
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.boton_crear_expedientes),
                    contentDescription = null,
                    tint = FigmaRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Eliminar Cliente", fontSize = 16.sp)
            }
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
    isMultiline: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isMultiline) 200.dp else 40.dp)
                .background(FigmaFieldBackground, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = if (isMultiline) Alignment.TopStart else Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = if (isMultiline) Alignment.Top else Alignment.CenterVertically,
                modifier = Modifier.padding(top = if (isMultiline) 8.dp else 0.dp)
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 15.sp
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField: @Composable () -> Unit ->
                        innerTextField()
                    }
                )
                if (isDropdown) {
                    Icon(
                        painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(90f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditClientScreenPreview() {
    LegallyTheme {
        EditClientScreen(clientId = "1")
    }
}

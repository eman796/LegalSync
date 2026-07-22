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
    var identityType by remember { mutableStateOf("Cédula") }
    var birthDate by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var documentNumber by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(clientId) {
        if (clientId != null && clientId != "new" && clientId.isNotEmpty()) {
            viewModel.loadClientById(clientId) { client ->
                client?.let {
                    name = it.name
                    lastName = it.lastName
                    personType = it.personType
                    birthDate = it.birthDate
                    nationality = it.nationality
                    email = it.email
                    phone = it.phone
                    description = it.description
                    // Assuming documentNumber might be lastName or stored elsewhere, 
                    // for now we'll use lastName as a placeholder for the second name/id
                    documentNumber = it.lastName 
                }
            }
        }
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
            personType = personType,
            onPersonTypeChange = { personType = it },
            identityType = identityType,
            onIdentityTypeChange = { identityType = it },
            birthDate = birthDate,
            onBirthDateChange = { birthDate = it },
            nationality = nationality,
            onNationalityChange = { nationality = it },
            email = email,
            onEmailChange = { email = it },
            phone = phone,
            onPhoneChange = { phone = it },
            documentNumber = documentNumber,
            onDocumentNumberChange = { documentNumber = it },
            description = description,
            onDescriptionChange = { description = it },
            onNavigateBack = onNavigateBack,
            onSaveClick = {
                val client = Client(
                    id = if (clientId == "new" || clientId == null) "" else clientId,
                    name = name,
                    lastName = documentNumber, // Using lastName for document number per current model
                    personType = personType,
                    birthDate = birthDate,
                    nationality = nationality,
                    email = email,
                    phone = phone,
                    description = description
                )
                if (clientId == null || clientId == "new") {
                    viewModel.createClient(client) { success, _ -> if (success) onNavigateBack() }
                } else {
                    viewModel.updateClient(client) { success, _ -> if (success) onNavigateBack() }
                }
            },
            onDeleteClick = {
                clientId?.let {
                    if (it != "new") {
                        viewModel.deleteClient(it) { success, _ -> if (success) onNavigateBack() }
                    }
                }
            }
        )
    }
}

@Composable
private fun EditClientContent(
    modifier: Modifier = Modifier,
    name: String = "",
    onNameChange: (String) -> Unit = {},
    personType: String = "",
    onPersonTypeChange: (String) -> Unit = {},
    identityType: String = "",
    onIdentityTypeChange: (String) -> Unit = {},
    birthDate: String = "",
    onBirthDateChange: (String) -> Unit = {},
    nationality: String = "",
    onNationalityChange: (String) -> Unit = {},
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    phone: String = "",
    onPhoneChange: (String) -> Unit = {},
    documentNumber: String = "",
    onDocumentNumberChange: (String) -> Unit = {},
    description: String = "",
    onDescriptionChange: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onDuplicateClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Header
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
                text = stringResource(id = R.string.edit_client_title),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.duplicate),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onDuplicateClick() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = R.string.cancel),
                color = FigmaGold,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onCancelClick() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // General Info Section
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.boton_expedientes_expedientes),
                contentDescription = null,
                tint = FigmaGold,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.general_info),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            label = stringResource(id = R.string.client_name_label),
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = stringResource(id = R.string.person_type_label),
                value = personType,
                onValueChange = onPersonTypeChange,
                modifier = Modifier.weight(1f),
                isDropdown = true
            )
            Spacer(modifier = Modifier.width(16.dp))
            InputField(
                label = stringResource(id = R.string.identity_label),
                value = identityType,
                onValueChange = onIdentityTypeChange,
                modifier = Modifier.weight(1f),
                isDropdown = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InputField(
                label = stringResource(id = R.string.birth_date_label),
                value = birthDate,
                onValueChange = onBirthDateChange,
                modifier = Modifier.weight(1f),
                isDropdown = true
            )
            Spacer(modifier = Modifier.width(16.dp))
            InputField(
                label = stringResource(id = R.string.nationality_label),
                value = nationality,
                onValueChange = onNationalityChange,
                modifier = Modifier.weight(1f),
                isDropdown = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.contact_section),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        InputField(
            label = stringResource(id = R.string.email_label),
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
                label = stringResource(id = R.string.phone_label),
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
            Spacer(modifier = Modifier.width(16.dp))
            InputField(
                label = stringResource(id = R.string.document_number_label),
                value = documentNumber,
                onValueChange = onDocumentNumberChange,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = stringResource(id = R.string.client_description_label),
            value = description,
            onValueChange = onDescriptionChange,
            isMultiline = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Text(
            text = stringResource(id = R.string.char_count, description.length, 1000),
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FigmaGold),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.boton_crear_expedientes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.save_client), fontSize = 16.sp)
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
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.boton_crear_expedientes),
                    contentDescription = null,
                    tint = FigmaRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.delete_case), fontSize = 16.sp)
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

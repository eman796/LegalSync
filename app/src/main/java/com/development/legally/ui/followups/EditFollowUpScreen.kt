package com.development.legally.ui.followups

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.FollowUp
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.navigation.LegallyBottomNavigationBar

private val FigmaBackground = Color(0xFF1C2632)
private val FigmaGold = Color(0xFF9E8D44)
private val FigmaFieldBackground = Color(0xFF171E27)
private val FigmaRed = Color(0xFFF50505)

@Composable
fun EditFollowUpScreen(
    caseId: String?,
    followUpId: String? = null,
    modifier: Modifier = Modifier,
    viewModel: FollowUpViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToClients: () -> Unit = {}
) {
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var responsibleUser by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
                currentRoute = "cases",
                onInicioClick = onNavigateToHome,
                onExpedientesClick = onNavigateToCases,
                onCrearClick = onNavigateToNewCase,
                onAgendaClick = onNavigateToAgenda,
                onClientesClick = onNavigateToClients
            )
        },
        containerColor = FigmaBackground
    ) { paddingValues ->
        EditFollowUpContent(
            modifier = modifier.padding(paddingValues),
            date = date,
            onDateChange = { date = it },
            description = description,
            onDescriptionChange = { description = it },
            responsibleUser = responsibleUser,
            onResponsibleUserChange = { responsibleUser = it },
            onNavigateBack = onNavigateBack,
            isLoading = isLoading,
            isEditMode = followUpId != null && followUpId != "new",
            onSaveClick = {
                if (validateFields(date, description, responsibleUser)) {
                    isLoading = true
                    if (caseId != null) {
                        val followUp = FollowUp(
                            id = if (followUpId == "new" || followUpId == null) "" else followUpId ?: "",
                            caseId = caseId,
                            date = date,
                            description = description,
                            responsibleUser = responsibleUser
                        )
                        if (followUpId == null || followUpId == "new") {
                            viewModel.createFollowUp(followUp) { success, error ->
                                isLoading = false
                                if (success) {
                                    onNavigateBack()
                                } else {
                                    errorMessage = error ?: "Error al crear seguimiento"
                                    showErrorDialog = true
                                }
                            }
                        } else {
                            viewModel.updateFollowUp(followUp) { success, error ->
                                isLoading = false
                                if (success) {
                                    onNavigateBack()
                                } else {
                                    errorMessage = error ?: "Error al actualizar seguimiento"
                                    showErrorDialog = true
                                }
                            }
                        }
                    }
                } else {
                    errorMessage = "Por favor completa todos los campos obligatorios"
                    showErrorDialog = true
                }
            },
            onDeleteClick = {
                if (followUpId != null && followUpId != "new") {
                    isLoading = true
                    viewModel.deleteFollowUp(followUpId) { success, error ->
                        isLoading = false
                        if (success) {
                            onNavigateBack()
                        } else {
                            errorMessage = error ?: "Error al eliminar seguimiento"
                            showErrorDialog = true
                        }
                    }
                }
            }
        )
    }
}

private fun validateFields(date: String, description: String, responsibleUser: String): Boolean {
    return date.isNotBlank() && description.isNotBlank() && responsibleUser.isNotBlank()
}

@Composable
private fun EditFollowUpContent(
    modifier: Modifier = Modifier,
    date: String = "",
    onDateChange: (String) -> Unit = {},
    description: String = "",
    onDescriptionChange: (String) -> Unit = {},
    responsibleUser: String = "",
    onResponsibleUserChange: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    isLoading: Boolean = false,
    isEditMode: Boolean = false,
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
                text = if (isEditMode) "Editar Seguimiento" else "Nuevo Seguimiento",
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
                text = "Detalles del Seguimiento",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            label = "Fecha *",
            value = date,
            onValueChange = onDateChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = "DD/MM/YYYY"
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Descripción *",
            value = description,
            onValueChange = onDescriptionChange,
            isMultiline = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = "Detalle del seguimiento..."
        )
        Text(
            text = "${description.length}/500",
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        InputField(
            label = "Usuario Responsable *",
            value = responsibleUser,
            onValueChange = onResponsibleUserChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Nombre del responsable"
        )

        Spacer(modifier = Modifier.height(32.dp))

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
                    Text(
                        text = if (isEditMode) "Actualizar Seguimiento" else "Crear Seguimiento",
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isEditMode) {
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
                    Text(text = "Eliminar Seguimiento", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
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
    placeholder: String = "",
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
                .height(if (isMultiline) 150.dp else 40.dp)
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
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = Color.Gray,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditFollowUpScreenPreview() {
    LegallyTheme {
        EditFollowUpScreen(caseId = "1", followUpId = "new")
    }
}

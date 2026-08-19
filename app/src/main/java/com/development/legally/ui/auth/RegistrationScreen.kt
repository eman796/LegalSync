package com.development.legally.ui.auth

import android.R.attr.password
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.ui.ClasesSupremas.BackButton
import com.development.legally.ui.ClasesSupremas.OutlinedText
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun RegistrationScreen(
    onBackClick: () -> Unit = {},
    registrationViewModel: RegistrationViewModel = viewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    val registrationState by registrationViewModel.registrationState.collectAsState()
    val scrollState = rememberScrollState()
    var password by remember { mutableStateOf("") }
    val errorMessage by remember { mutableStateOf("") }
    
    // 1. MOVIMIENTO AUTOMÁTICO: Reducimos el espacio superior cuando sale el teclado
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val topSpacerHeight by animateDpAsState(
        targetValue = if (isKeyboardVisible) 20.dp else 220.dp,
        label = "TopSpacerAnimation"
    )

    LaunchedEffect(registrationState) {
        if (registrationState is RegistrationViewModel.RegistrationState.Success) {
            showSuccessDialog = true
        }
    }

    val backgroundColor = Color(0xFF1C2632)
    val goldColor = Color(0xFF9E8D44)
    val inputBackgroundColor = Color(0xFF171E27)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 25.dp)
        ) {
            Spacer(modifier = Modifier.height(topSpacerHeight))

            OutlinedText(
                text = "SOLICITAR REGISTRO",
                mainColor = Color.White,
                outlineColor = goldColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                strokeWidth = 4f
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            Column {
                OutlinedText(
                    text = "Nombre completo",
                    mainColor = Color.White,
                    outlineColor = goldColor,
                    fontSize = 18.sp,
                    strokeWidth = 3f
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp).border(1.dp, goldColor, RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = inputBackgroundColor,
                        unfocusedContainerColor = inputBackgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = goldColor
                    ),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                OutlinedText(
                    text = "Correo electrónico",
                    mainColor = Color.White,
                    outlineColor = goldColor,
                    fontSize = 18.sp,
                    strokeWidth = 3f
                )
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp).border(1.dp, goldColor, RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = inputBackgroundColor,
                        unfocusedContainerColor = inputBackgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = goldColor
                    ),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )
            }

            if (registrationState is RegistrationViewModel.RegistrationState.Error) {
                Text(
                    text = (registrationState as RegistrationViewModel.RegistrationState.Error).message,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
            Column {
                OutlinedText(
                    text = "Contraseña en caso de ser aprobados",
                    fontSize = 16.sp,
                    outlineColor = goldColor,
                    mainColor = Color.White,
                    strokeWidth = 3f
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(width = 1.dp, color = goldColor, shape = RoundedCornerShape(20.dp)),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = inputBackgroundColor,
                        unfocusedContainerColor = inputBackgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = goldColor
                    ),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )
            }


            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            val isLoading = registrationState is RegistrationViewModel.RegistrationState.Loading
            Button(
                onClick = { if (!isLoading) registrationViewModel.requestRegistration(fullName, email, password) },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = goldColor,
                    disabledContainerColor = goldColor.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(64.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color(0xFF171E27))
                } else {
                    Text(
                        text = "ENVIAR SOLICITUD",
                        fontSize = 16.sp,
                        color = Color(0xFF171E27),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
        BackButton(onClick = onBackClick)
    }

    // Diálogo de confirmación
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false; onBackClick() },
            title = { Text("Solicitud Enviada") },
            text = { Text("Tu solicitud de acceso ha sido enviada.") },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false; onBackClick() }) {
                    Text("ENTENDIDO", color = goldColor)
                }
            },
            containerColor = inputBackgroundColor,
            titleContentColor = Color.White,
            textContentColor = Color.LightGray
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
fun RegistrationScreenPreview() {
    LegallyTheme {
        RegistrationScreen()
    }
}

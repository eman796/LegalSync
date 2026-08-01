package com.development.legally.ui.auth

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
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onBackClick: () -> Unit = {},
    loginViewModel: LoginViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val loginState by loginViewModel.loginState.collectAsState()
    val scrollState = rememberScrollState()
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val topSpacerHeight by animateDpAsState(
        targetValue = if (isKeyboardVisible) 100.dp else 260.dp,
        label = "TopSpacerAnimation"
    )

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginViewModel.LoginState.Success -> {
                onLoginSuccess()
                loginViewModel.resetState()
            }
            is LoginViewModel.LoginState.Error -> {
                errorMessage = (loginState as LoginViewModel.LoginState.Error).message
            }
            else -> {}
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

            // Username Label con borde dorado
            Column {
                OutlinedText(
                    text = stringResource(id = R.string.username),
                    fontSize = 20.sp,
                    outlineColor = goldColor,
                    mainColor = Color.White, // Argumento corregido
                    strokeWidth = 3f
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .border(width = 1.dp, color = goldColor, shape = RoundedCornerShape(20.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = goldColor,
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

            Spacer(modifier = Modifier.height(24.dp))

            // Password Label con borde dorado
            Column {
                OutlinedText(
                    text = stringResource(id = R.string.password),
                    fontSize = 16.sp,
                    outlineColor = goldColor,
                    mainColor = Color.White, // Argumento corregido
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

            val isLoading = loginState is LoginViewModel.LoginState.Loading
            Button(
                onClick = { if (!isLoading) loginViewModel.login(username, password) },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = goldColor,
                    disabledContainerColor = goldColor.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(64.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color(0xFF171E27))
                } else {
                    Text(text = "INICIAR SESIÓN", fontSize = 14.sp, color = Color(0xFF171E27), fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
        BackButton(onClick = onBackClick)
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
fun LoginScreenPreview() {
    LegallyTheme {
        LoginScreen()
    }
}

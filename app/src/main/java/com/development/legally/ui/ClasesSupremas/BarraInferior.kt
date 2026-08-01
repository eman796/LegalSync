package com.development.legally.ui.ClasesSupremas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.development.legally.R
//Esta clase es la barra inferior, que contiene los 5 botones principales
@Composable
fun UserAction(
    onLogoutConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    goldColor: Color = Color(0xFF9E8D44)
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .width(15.dp)
            .height(19.dp)
            .background(Color.Transparent)
            .clickable { showDialog = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.boton_usuario_expedientes),
            contentDescription = "Usuario",
            tint = goldColor,
            modifier = Modifier.fillMaxSize()
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cerrar Sesión", color = Color.White) },
            text = { Text("¿Estás seguro de que deseas salir de la aplicación?", color = Color.White) },
            confirmButton = {
                TextButton(onClick = { 
                    showDialog = false
                    onLogoutConfirm() 
                }) {
                    Text("SÍ, SALIR", color = goldColor, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("CANCELAR", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF171E27)
        )
    }
}

@Composable
fun NotificationAction(
    modifier: Modifier = Modifier,
    notifications: List<String> = listOf("Cita programada a las 10:00 AM", "Nuevo expediente asignado"),
    goldColor: Color = Color(0xFF9E8D44)
) {
    var isExpanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .width(23.dp)
            .height(19.2.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Icon(
            painter = painterResource(
                id = if (isExpanded) R.drawable.ic_notifications_on else R.drawable.ic_notifications_off
            ),
            contentDescription = "Notificaciones",
            tint = Color.Unspecified,
            modifier = Modifier.fillMaxSize()
        )
        
        if (isExpanded) {
            Popup(
                alignment = Alignment.TopEnd,
                onDismissRequest = { isExpanded = false },
                offset = IntOffset(0, with(density) { 45.dp.roundToPx() })
            ) {
                Card(
                    modifier = Modifier
                        .width(280.dp)
                        .padding(end = 12.dp)
                        .border(1.dp, goldColor, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF171E27))
                ) {
                    LazyColumn(modifier = Modifier.padding(12.dp)) {
                        item {
                            Text("Notificaciones", color = goldColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                        }
                        items(notifications) { notification ->
                            Text(notification, color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(vertical = 4.dp))
                            HorizontalDivider(color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainSearchBar(
    title: String,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    goldColor: Color = Color(0xFF9E8D44)
) {
    var query by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    // Detectar visibilidad del teclado
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    // Si el teclado se cierra manualmente, cancelamos la búsqueda
    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible && query.isNotEmpty()) {
            query = ""
            onSearch("")
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .background(Color(0xFF171E27), RoundedCornerShape(24.dp))
            .border(1.dp, goldColor, RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // "X" a la izquierda: limpia y oculta teclado
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { 
                        query = ""
                        onSearch("")
                        keyboardController?.hide()
                    }
            )
            
            Box(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                if (query.isEmpty()) {
                    OutlinedText(
                        text = title,
                        mainColor = Color.White,
                        outlineColor = goldColor,
                        fontSize = 13.sp,
                        strokeWidth = 2f
                    )
                }
                
                BasicTextField(
                    value = query,
                    onValueChange = { 
                        query = it
                        onSearch(it)
                    },
                    textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = goldColor,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { 
                        onSearch(query)
                        keyboardController?.hide()
                    }
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    goldColor: Color = Color(0xFF9E8D44)
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedText(
            text = title,
            mainColor = Color.White,
            outlineColor = goldColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            strokeWidth = 3f
        )
    }
}

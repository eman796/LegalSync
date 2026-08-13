package com.development.legally.ui.ClasesSupremas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.NotificationItem
import com.development.legally.ui.agenda.AgendaViewModel

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
    agendaViewModel: AgendaViewModel = viewModel(),
    onNotificationClick: (String) -> Unit = {},
    goldColor: Color = Color(0xFF9E8D44)
) {
    var isExpanded by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val uiState by agendaViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        agendaViewModel.loadEvents()
    }

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
                        .width(300.dp)
                        .heightIn(max = 400.dp)
                        .padding(end = 12.dp)
                        .border(1.dp, goldColor, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF171E27))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "Notificaciones de Eventos", 
                            color = goldColor, 
                            fontWeight = FontWeight.Bold, 
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        if (uiState.notifications.isEmpty()) {
                            Text(
                                "No hay notificaciones próximas", 
                                color = Color.Gray, 
                                fontSize = 14.sp,
                                modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                                items(uiState.notifications) { notification ->
                                    NotificationListItem(notification) {
                                        isExpanded = false
                                        onNotificationClick(it)
                                    }
                                    HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationListItem(
    notification: NotificationItem,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(notification.eventId) }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = notification.title,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = notification.message,
            color = Color.LightGray,
            fontSize = 13.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
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
            Icon(
                painter = painterResource(id = R.drawable.ic_close_figma),
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
                painter = painterResource(id = R.drawable.group_5),
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

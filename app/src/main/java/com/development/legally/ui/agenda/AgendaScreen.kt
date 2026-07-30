package com.development.legally.ui.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.components.*
import com.development.legally.ui.Nuevo.NewOverlay

@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToClients: () -> Unit = {},
    onNavigateToEditClient: (String) -> Unit = {}
) {
    var showNewMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 17.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserAction(onLogoutConfirm = onLogout)
                    SectionHeader(title = "Agenda del abogado", modifier = Modifier.weight(1f))
                    NotificationAction()
                }
            },
            bottomBar = {
                LegallyBottomNavigationBar(
                    currentRoute = "agenda",
                    onInicioClick = onNavigateToHome,
                    onExpedientesClick = onNavigateToCases,
                    onCrearClick = { showNewMenu = true },
                    onAgendaClick = { },
                    onClientesClick = onNavigateToClients
                )
            },
            containerColor = Color(0xFF1C2632)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 17.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                MainSearchBar(title = "Buscar eventos, tareas...", onSearch = { })

                Spacer(modifier = Modifier.height(24.dp))

                FilterSectionRow(title = "Filtrar por:") {
                    FilterDropdown(label = "Fecha", options = listOf("Hoy", "Mañana", "Esta semana"), onOptionSelected = {})
                    FilterDropdown(label = "Tipo", options = listOf("Tarea", "Audiencia", "Cita"), onOptionSelected = {})
                    FilterDropdown(label = "URGENTE", options = listOf("Sí", "No"), isUrgent = true, onOptionSelected = {})
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color(0xFF171E27))) {
                    AgendaList()
                }
            }
        }

        if (showNewMenu) {
            NewOverlay(
                onClose = { showNewMenu = false },
                onNewClient = { showNewMenu = false; onNavigateToEditClient("new") },
                onNewEvent = { showNewMenu = false },
                onNewCase = { showNewMenu = false; onNavigateToNewCase() }
            )
        }
    }
}

@Composable
fun AgendaList() {
    val items = listOf(
        AgendaItemData("08:00 - 09:00", "Exp. 25-000444-033-PE", "Christian Bulgarelli", "Ocupado", Color.Red),
        AgendaItemData("10:30 - 11:30", "Exp. 24-001234-004-CI", "Maria Perez", "Disponible", Color.Green),
        AgendaItemData("13:00 - 14:00", "Exp. 23-005566-012-FA", "Juan Soto", "Normal", Color.Yellow)
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { item ->
            AgendaListItem(item)
        }
    }
}

@Composable
fun AgendaListItem(data: AgendaItemData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .drawBehind {
                drawLine(color = Color(0xFF9E8D44), start = Offset(0f, size.height), end = Offset(size.width, size.height), strokeWidth = 1.dp.toPx())
            }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = data.duration, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = data.caseId, color = Color.White, fontSize = 14.sp)
                Text(text = data.client, color = Color.Gray, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(data.statusColor))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = data.status, color = Color.White, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Icon(painter = painterResource(id = R.drawable.ic_arrow_right_gold), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(20.dp))
        }
    }
}

data class AgendaItemData(val duration: String, val caseId: String, val client: String, val status: String, val statusColor: Color)

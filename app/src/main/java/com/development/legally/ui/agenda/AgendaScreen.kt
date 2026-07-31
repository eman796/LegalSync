package com.development.legally.ui.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.development.legally.ui.theme.*
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
    onNavigateToEditClient: (String) -> Unit = {},
    onNavigateToEditEvent: (String) -> Unit = {}
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
            containerColor = FigmaBackground
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

                Box(modifier = Modifier.fillMaxWidth().weight(1f).background(FigmaBackground)) {
                    AgendaList(onEventClick = onNavigateToEditEvent)
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
fun AgendaList(onEventClick: (String) -> Unit) {
    val items = listOf(
        AgendaItemData("id1", "Todo el día", "Exp. 25-000444-033-PE", "Christian Bullgarelli vs Federico cruz", Color(0xFFFFB74D)),
        AgendaItemData("id2", "10:30 - 11:30", "Exp. 24-001234-004-CI", "Maria Perez vs Banco Central", Color(0xFF81C784)),
        AgendaItemData("id3", "13:00 - 14:00", "Exp. 23-005566-012-FA", "Juan Soto vs Ana Rojas", Color(0xFFFFF176))
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            MasterAgendaItem(
                time = item.duration,
                caseNumber = item.caseId,
                title = item.description,
                statusColor = item.statusColor,
                onClick = { onEventClick(item.id) }
            )
        }
    }
}

data class AgendaItemData(
    val id: String,
    val duration: String,
    val caseId: String, 
    val description: String, 
    val statusColor: Color
)

@Preview(showBackground = true)
@Composable
fun AgendaScreenPreview() {
    LegallyTheme {
        AgendaScreen()
    }
}

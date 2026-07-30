package com.development.legally.ui.clients

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.components.*
import com.development.legally.ui.Nuevo.NewOverlay

@Composable
fun ClientsScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToEditClient: (String) -> Unit = {},
    viewModel: ClientViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
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
                    SectionHeader(title = "Clientes del abogad@", modifier = Modifier.weight(1f))
                    NotificationAction()
                }
            },
            bottomBar = {
                LegallyBottomNavigationBar(
                    currentRoute = "clients",
                    onInicioClick = onNavigateToHome,
                    onExpedientesClick = onNavigateToCases,
                    onCrearClick = { showNewMenu = true },
                    onAgendaClick = onNavigateToAgenda,
                    onClientesClick = {}
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
                
                MainSearchBar(title = "Buscar clientes...", onSearch = { viewModel.searchClients(it) })

                Spacer(modifier = Modifier.height(24.dp))

                // Filtros con la clase reutilizable que recibe ArrayList
                FilterSectionRow(title = "Filtrar por:") {
                    FilterDropdown(label = "Fecha", options = listOf("Recientes", "Antiguos"), onOptionSelected = {})
                    FilterDropdown(label = "Cliente", options = listOf("A-Z", "Z-A"), onOptionSelected = {})
                    FilterDropdown(label = "Tipo", options = listOf("Física", "Jurídica"), onOptionSelected = {})
                    FilterDropdown(label = "URGENTE", options = listOf("Sí", "No"), isUrgent = true, onOptionSelected = {})
                }

                Spacer(modifier = Modifier.height(24.dp))

                FilterSectionRow(title = "Ordenar por:") {
                    FilterDropdown(label = "Nombre", options = listOf("A-Z", "Z-A"), onOptionSelected = {})
                    FilterDropdown(label = "Cliente", options = listOf("A-Z", "Z-A"), onOptionSelected = {})
                    FilterDropdown(label = "Tipo", options = listOf("Física", "Jurídica"), onOptionSelected = {})
                    FilterDropdown(label = "URGENTE", options = listOf("Sí", "No"), isUrgent = true, onOptionSelected = {})
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth().weight(1f).background(Color(0xFF171E27))) {
                    if (uiState.loading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF9E8D44))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(uiState.filtered) { client ->
                                // Uso de la Clase Maestra de Items
                                MasterClientItem(
                                    name = "${client.name} ${client.lastName}",
                                    activeCount = "5 expedientes activos",
                                    summary = "1 sucesorio, 1 posesorio, 1 divorcio, 1 pensión alimentaria",
                                    status = "Activo",
                                    onClick = { onNavigateToEditClient(client.id) }
                                )
                            }
                        }
                    }
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

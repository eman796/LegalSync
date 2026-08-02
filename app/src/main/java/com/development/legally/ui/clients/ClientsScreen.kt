package com.development.legally.ui.clients

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.ui.theme.*
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.ClasesSupremas.*
import com.development.legally.ui.Nuevo.NewOverlay

@Composable
fun ClientsScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToNewClient: () -> Unit = {},
    onNavigateToNewEvent: () -> Unit = {},
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
                    SectionHeader(title = "Clientes", modifier = Modifier.weight(1f))
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
            containerColor = FigmaBackground
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 17.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                MainSearchBar(title = "Buscar clientes...", onSearch = { viewModel.searchClients(it) })
                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth().weight(1f).background(FigmaBackground)) {
                    if (uiState.loading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = FigmaGold)
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(uiState.filtered) { client ->
                                MasterClientItem(
                                    name = "${client.name} ${client.lastName}",
                                    activeCount = "5 expedientes activos",
                                    summary = "Gestión legal activa",
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
                onNewClient = {
                    showNewMenu = false
                    onNavigateToNewClient()
                },
                onNewEvent = {
                    showNewMenu = false
                    onNavigateToNewEvent()
                },
                onNewCase = {
                    showNewMenu = false
                    onNavigateToNewCase()
                }
            )
        }
    }
}

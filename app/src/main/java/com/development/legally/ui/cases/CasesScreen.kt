package com.development.legally.ui.cases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.development.legally.ui.theme.*
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.ClasesSupremas.*
import com.development.legally.ui.Nuevo.NewOverlay

@Composable
fun CasesScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToNewClient: () -> Unit = {},
    onNavigateToNewEvent: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToClients: () -> Unit = {},
    onNavigateToEditCase: (String) -> Unit = {},
    viewModel: CasosViewModel = viewModel() // Instanciación del ViewModel según las instrucciones
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargamos los datos solo al iniciar la pantalla real
    LaunchedEffect(Unit) {
        viewModel.loadCasos()
    }

    // Pasamos el ViewModel directamente a la interfaz de contenido
    CasesContent(
        uiState = uiState,
        viewModel = viewModel,
        onLogout = onLogout,
        onNavigateToHome = onNavigateToHome,
        onNavigateToNewCase = onNavigateToNewCase,
        onNavigateToNewClient = onNavigateToNewClient,
        onNavigateToNewEvent = onNavigateToNewEvent,
        onNavigateToAgenda = onNavigateToAgenda,
        onNavigateToClients = onNavigateToClients,
        onNavigateToEditCase = onNavigateToEditCase
    )
}

@Composable
fun CasesContent(
    uiState: CasosUiState,
    viewModel: CasosViewModel?, // Nullable para soportar Preview
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToNewClient: () -> Unit = {},
    onNavigateToNewEvent: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToClients: () -> Unit = {},
    onNavigateToEditCase: (String) -> Unit = {}
) {
    var showNewMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    SectionHeader(title = "Expedientes", modifier = Modifier.weight(1f))
                    NotificationAction()
                }
            },
            bottomBar = {
                LegallyBottomNavigationBar(
                    currentRoute = "cases",
                    onInicioClick = onNavigateToHome,
                    onExpedientesClick = { },
                    onCrearClick = { showNewMenu = true },
                    onAgendaClick = onNavigateToAgenda,
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

                // Uso directo del ViewModel para acciones
                MainSearchBar(
                    title = "Buscar expedientes...",
                    onSearch = { viewModel?.updateSearchQuery(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                FilterSectionRow(title = "Filtrar por:") {
                    FilterDropdown(
                        label = "Estado",
                        options = listOf("Todos", "En proceso", "Pendiente", "Finalizado", "Archivado"),
                        onOptionSelected = { viewModel?.updateStatusFilter(it) }
                    )
                    FilterDropdown(
                        label = "Prioridad",
                        options = listOf("Todas", "Baja", "Media", "Alta", "Urgente"),
                        onOptionSelected = { viewModel?.updatePriorityFilter(it) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(FigmaBackground)) {
                    if (uiState.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = FigmaGold
                        )
                    } else if (uiState.error != null) {
                        Text(
                            text = uiState.error ?: "Error al cargar expedientes",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.filtered) { case ->
                                MasterCaseItem(
                                    caseNumber = case.caseNumber,
                                    description = case.description,
                                    status = case.status.uppercase(),
                                    updateDate = "Actualizado",
                                    onClick = { onNavigateToEditCase(case.id) }
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

@Preview(showBackground = true)
@Composable
fun CasesPreview() {
    LegallyTheme {
        // En el Preview pasamos null al ViewModel para que no intente usar Firebase
        CasesContent(
            uiState = CasosUiState(
                loading = false,
                filtered = emptyList()
            ),
            viewModel = null
        )
    }
}

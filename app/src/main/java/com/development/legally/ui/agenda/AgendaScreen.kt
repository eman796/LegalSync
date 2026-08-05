package com.development.legally.ui.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.data.model.Event
import com.development.legally.ui.theme.*
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.ClasesSupremas.*
import com.development.legally.ui.Nuevo.NewOverlay

@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToNewClient: () -> Unit = {},
    onNavigateToNewEvent: () -> Unit = {},
    onNavigateToClients: () -> Unit = {},
    onNavigateToEditEvent: (String) -> Unit = {},
    viewModel: AgendaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar eventos reales desde Firebase al iniciar
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    AgendaContent(
        uiState = uiState,
        onLogout = onLogout,
        onNavigateToHome = onNavigateToHome,
        onNavigateToCases = onNavigateToCases,
        onNavigateToNewCase = onNavigateToNewCase,
        onNavigateToNewClient = onNavigateToNewClient,
        onNavigateToNewEvent = onNavigateToNewEvent,
        onNavigateToClients = onNavigateToClients,
        onNavigateToEditEvent = onNavigateToEditEvent,
        onSearch = { viewModel.updateSearchQuery(it) },
        onDayFilterSelected = { viewModel.updateDayFilter(it) },
        onTypeFilterSelected = { viewModel.updateTypeFilter(it) }
    )
}

@Composable
fun AgendaContent(
    uiState: AgendaUiState,
    onLogout: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToCases: () -> Unit,
    onNavigateToNewCase: () -> Unit,
    onNavigateToNewClient: () -> Unit,
    onNavigateToNewEvent: () -> Unit,
    onNavigateToClients: () -> Unit,
    onNavigateToEditEvent: (String) -> Unit,
    onSearch: (String) -> Unit,
    onDayFilterSelected: (String) -> Unit,
    onTypeFilterSelected: (String) -> Unit
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
                    SectionHeader(title = "Agenda", modifier = Modifier.weight(1f))
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

                MainSearchBar(
                    title = "Buscar eventos, tareas...",
                    onSearch = onSearch
                )

                Spacer(modifier = Modifier.height(16.dp))

                FilterSectionRow(title = "Filtrar por:") {
                    FilterDropdown(
                        label = "Día",
                        options = listOf("Hoy", "Mañana", "Esta semana", "Mes"),
                        onOptionSelected = onDayFilterSelected
                    )
                    FilterDropdown(
                        label = "Tipo",
                        options = listOf("Todos", "Audiencia", "Reunión", "Tarea"),
                        onOptionSelected = onTypeFilterSelected
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(modifier = Modifier.fillMaxWidth().weight(1f).background(FigmaBackground)) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = FigmaGold
                        )
                    } else if (uiState.error != null) {
                        Text(
                            text = uiState.error ?: "Error al cargar la agenda",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        AgendaList(
                            events = uiState.filteredEvents,
                            onEventClick = onNavigateToEditEvent
                        )
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

@Composable
fun AgendaList(
    events: List<Event>,
    onEventClick: (String) -> Unit
) {
    if (events.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay eventos programados", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(events) { event ->
                MasterAgendaItem(
                    time = event.duracion,
                    caseNumber = event.casoRelacionado,
                    title = event.titulo,
                    statusColor = when(event.tipo) {
                        "Audiencia" -> Color(0xFFFFB74D)
                        "Reunión" -> Color(0xFF81C784)
                        else -> Color(0xFFFFF176)
                    },
                    onClick = { onEventClick(event.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AgendaScreenPreview() {
    LegallyTheme {
        // Preview con tus datos reales de Firebase para verificar visualmente
        AgendaContent(
            uiState = AgendaUiState(
                isLoading = false,
                filteredEvents = listOf(
                    Event(
                        titulo = "Jihyo Closet",
                        tipo = "Audiencia",
                        duracion = "30 min",
                        casoRelacionado = "En desarrollo...",
                        estado = "Completado"
                    )
                )
            ),
            onLogout = {}, onNavigateToHome = {}, onNavigateToCases = {},
            onNavigateToNewCase = {}, onNavigateToNewClient = {}, onNavigateToNewEvent = {},
            onNavigateToClients = {}, onNavigateToEditEvent = {},
            onSearch = {}, onDayFilterSelected = {}, onTypeFilterSelected = {}
        )
    }
}

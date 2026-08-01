package com.development.legally.ui.cases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.development.legally.ui.theme.*
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.ClasesSupremas.*
import com.development.legally.ui.Nuevo.NewOverlay

@Composable
fun CasesScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToClients: () -> Unit = {},
    onNavigateToEditCase: (String) -> Unit = {},
    onNavigateToEditClient: (String) -> Unit = {}
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
                
                MainSearchBar(
                    title = "Buscar expedientes...",
                    onSearch = { }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // SECCIÓN FILTROS (Clase reutilizable)
                FilterSectionRow(title = "Filtrar por:") {
                    FilterDropdown(
                        label = "Fecha", 
                        options = listOf("Hoy", "Ayer", "Esta semana", "Este mes"), 
                        onOptionSelected = {}
                    )
                    FilterDropdown(
                        label = "Cliente", 
                        options = listOf("Recientes", "A-Z"), 
                        onOptionSelected = {}
                    )
                    FilterDropdown(
                        label = "Tipo", 
                        options = listOf("Penal", "Civil", "Laboral", "Familia"), 
                        onOptionSelected = {}
                    )
                    FilterDropdown(
                        label = "URGENTE", 
                        options = listOf("Sí", "No"), 
                        isUrgent = true, 
                        onOptionSelected = {}
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // NUEVA SECCIÓN: ORDENAR POR
                FilterSectionRow(title = "Ordenar por:") {
                    FilterDropdown(
                        label = "Actualización", 
                        options = listOf("Más recientes primero", "Más antiguos primero"), 
                        onOptionSelected = {}
                    )
                    FilterDropdown(
                        label = "Expediente", 
                        options = listOf("Número (A-Z)", "Número (Z-A)"), 
                        onOptionSelected = {}
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                val dummyCases = listOf(
                    "25-000044-033-PE" to "Christian Bullgarelli vs Federico cruz A.K.A Choreco",
                    "20-000115-1218-PE" to "Procuraduría vs Luis Guillermo Solís Rivera",
                    "25-002920-0175-PE" to "Rodrigo Arias Sánchez vs STEPHAN"
                )

                Box(modifier = Modifier.fillMaxWidth().weight(1f).background(FigmaBackground)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(dummyCases) { (id, desc) ->
                            MasterCaseItem(
                                caseNumber = id,
                                description = desc,
                                status = "ACTIVO",
                                updateDate = "Ayer",
                                onClick = { onNavigateToEditCase(id) }
                            )
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

@Preview(showBackground = true)
@Composable
fun CasesScreenPreview() {
    LegallyTheme {
        CasesScreen()
    }
}

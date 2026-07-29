package com.development.legally.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.Nuevo.NewOverlay
import com.development.legally.ui.theme.*
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.components.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
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
                    SectionHeader(title = "Inicio", modifier = Modifier.weight(1f))
                    NotificationAction()
                }
            },
            bottomBar = { 
                LegallyBottomNavigationBar(
                    currentRoute = "home",
                    onInicioClick = { },
                    onExpedientesClick = onNavigateToCases,
                    onCrearClick = { showNewMenu = true },
                    onAgendaClick = onNavigateToAgenda,
                    onClientesClick = onNavigateToClients
                ) 
            },
            containerColor = FigmaBackground
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 17.dp, vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { 
                    Spacer(modifier = Modifier.height(16.dp)) 
                    MainSearchBar(title = "Buscar expedientes, clientes...", onSearch = {})
                }
                
                item { SectionHeader(title = "¿Qué hay para hoy?") }
                item { NextEventCard() }

                // TEXTO "Que sigue ahora"
                item { 
                    Text(
                        text = "Que sigue ahora",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                // TARJETAS REPLACING GOLD RECTANGLES
                item { StatsRow() }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Casos más recientes", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Ver todos", 
                            color = FigmaGold, 
                            fontSize = 14.sp, 
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToCases() }
                        )
                    }
                }
                item { RecentCasesList() }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        if (showNewMenu) {
            NewOverlay(
                onClose = { showNewMenu = false },
                onNewClient = { showNewMenu = false },
                onNewEvent = { showNewMenu = false },
                onNewCase = {
                    showNewMenu = false
                    onNavigateToNewCase()
                }
            )
        }
    }
}

@Composable
fun StatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Tarjeta: Expedientes Activos
        StatCard(
            modifier = Modifier.weight(1f), 
            count = "+1k", 
            labelLines = listOf("Expedientes", "Activos"),
            icon = painterResource(id = R.drawable.ic_card_folder)
        )
        // Tarjeta: Audiencias Para hoy
        StatCard(
            modifier = Modifier.weight(1f), 
            count = "X", 
            labelLines = listOf("Audiencias", "Para: hoy"),
            icon = painterResource(id = R.drawable.ic_card_calendar)
        )
        // Tarjeta: Compromisos Próximamente
        StatCard(
            modifier = Modifier.weight(1f), 
            count = "+1k", 
            labelLines = listOf("Compromisos", "Próximamente"),
            icon = painterResource(id = R.drawable.ic_card_clock)
        )
        // Tarjeta: Tareas Pendientes
        StatCard(
            modifier = Modifier.weight(1f), 
            count = "+1k", 
            labelLines = listOf("Tareas", "Pendientes"),
            icon = painterResource(id = R.drawable.ic_card_tasks)
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier, 
    count: String, 
    labelLines: List<String>, 
    icon: Painter
) {
    Card(
        modifier = modifier.height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171E27)),
        border = BorderStroke(1.dp, FigmaGold)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icono Hollow Dorado del Figma
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified, // Respeta el color dorado definido en el XML
                modifier = Modifier.size(60.dp).padding(top = 4.dp)
            )
            
            Text(
                text = count, 
                color = Color.White, 
                fontSize = 24.sp, 
                fontWeight = FontWeight.ExtraBold
            )
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                labelLines.forEach { line ->
                    Text(
                        text = line, 
                        color = Color.White, 
                        fontSize = 11.sp, 
                        textAlign = TextAlign.Center, 
                        lineHeight = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun NextEventCard() {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, FigmaGold, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171E27))
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
                Text(text = "Mayo de 2026", color = Color.Gray, fontSize = 10.sp)
                Text(text = "Martes", color = Color.White, fontSize = 14.sp)
                Text(text = "20", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
            VerticalDivider(color = FigmaGold, modifier = Modifier.height(70.dp).padding(horizontal = 12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "hh:mm:a/p", color = Color.Gray, fontSize = 12.sp)
                Text(text = "Audiencia Importante", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RecentCasesList() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RecentCaseItem("25-000044-033-PE", "Hoy")
        RecentCaseItem("20-000115-1218-PE", "Ayer")
    }
}

@Composable
fun RecentCaseItem(id: String, update: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(42.dp).background(Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = id, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(text = "Actualizado: $update", color = Color.Gray, fontSize = 10.sp)
        }
    }
}

@Preview(showBackground = true, widthDp = 389, heightDp = 879)
@Composable
fun HomeScreenPreview() {
    LegallyTheme {
        HomeScreen()
    }
}

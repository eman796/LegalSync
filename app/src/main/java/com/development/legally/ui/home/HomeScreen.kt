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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    onNavigateToEditClient: (String) -> Unit = {},
    onNavigateToEditCase: (String) -> Unit = {},
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
                
                // 1. SOLUCIÓN BUG CALENDARIO
                item { 
                    NextEventCard(onClick = { onNavigateToEditEvent("event_id_123") }) 
                }

                item { 
                    Text(
                        text = "Que sigue ahora",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                // 2. SOLUCIÓN BUG TARJETAS
                item { 
                    StatsRow(
                        onNavigateToCases = onNavigateToCases,
                        onNavigateToAgenda = onNavigateToAgenda
                    ) 
                }
                
                // MÓDULO: CASOS MAS RECIENTES (3 elementos)
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Casos mas recientes", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Ver más", 
                                color = FigmaGold, 
                                fontSize = 14.sp, 
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onNavigateToCases() }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        RecentCasesList(onCaseClick = onNavigateToEditCase)
                    }
                }

                // MÓDULO: TAREAS PENDIENTES (3 elementos)
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Tareas pendientes", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Ver todo", 
                                color = FigmaGold, 
                                fontSize = 14.sp, 
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onNavigateToAgenda() }
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PendingTasksList(onTaskClick = { onNavigateToEditCase("case_id_linked") })
                    }
                }
                
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
fun NextEventCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, FigmaGold, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171E27))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(95.dp)) {
                Text(text = "Mayo de 2026", color = Color.White, fontSize = 12.sp)
                Text(text = "Martes", color = Color.White, fontSize = 15.sp)
                Text(text = "20", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            }
            Box(modifier = Modifier.width(1.dp).height(80.dp).background(Color.White.copy(alpha = 0.5f)))
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(text = "hh:mm:a/p", color = Color.White, fontSize = 14.sp)
                Text(text = "Lorem Ipsum", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "Caso numero Lorem Ipsum", color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.ic_location_pin), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "HEARTBREAK AVENUE, TWICELAND", color = Color.White, fontSize = 10.sp, maxLines = 1)
                }
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.height(80.dp)) {
                Box(modifier = Modifier.drawBehind { drawRoundRect(color = Color(0xFF9E8D44), style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)), cornerRadius = CornerRadius(8.dp.toPx())) }.padding(horizontal = 10.dp, vertical = 4.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Audiencia", color = Color.White, fontSize = 11.sp)
                }
                Icon(painter = painterResource(id = R.drawable.ic_arrow_right_gold), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun StatsRow(onNavigateToCases: () -> Unit, onNavigateToAgenda: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatCard(modifier = Modifier.weight(1f), count = "+1k", labelLines = listOf("Expedien", "tes", "Activos"), icon = painterResource(id = R.drawable.ic_card_folder_figma), onClick = onNavigateToCases)
        StatCard(modifier = Modifier.weight(1f), count = "X", labelLines = listOf("Audien", "cias", "Para: hoy"), icon = painterResource(id = R.drawable.ic_card_calendar_figma), onClick = onNavigateToAgenda)
        StatCard(modifier = Modifier.weight(1f), count = "+1k", labelLines = listOf("Compromi", "sos", "Próximamente"), icon = painterResource(id = R.drawable.ic_card_clock_figma), onClick = onNavigateToAgenda)
        StatCard(modifier = Modifier.weight(1f), count = "+1k", labelLines = listOf("Tareas", "Pendientes"), icon = painterResource(id = R.drawable.ic_card_tasks_figma), onClick = onNavigateToCases)
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, count: String, labelLines: List<String>, icon: Painter, onClick: () -> Unit = {}) {
    Card(modifier = modifier.height(210.dp).clickable { onClick() }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF171E27)), border = BorderStroke(1.dp, FigmaGold)) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            Icon(painter = icon, contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(75.dp).padding(top = 4.dp))
            Text(text = count, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                labelLines.forEach { line -> Text(text = line, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 14.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun RecentCasesList(onCaseClick: (String) -> Unit) {
    val cases = listOf("25-000044-033-PE", "20-000115-1218-PE", "25-002920-0175-PE")
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        cases.forEach { id ->
            RecentCaseItem(id = id, onClick = { onCaseClick(id) })
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
        }
    }
}

@Composable
fun RecentCaseItem(id: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(42.dp).background(FigmaGold.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Icon(painter = painterResource(id = R.drawable.ic_gavel), contentDescription = null, tint = FigmaGold, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = id, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(text = "Christian Bullgarelli vs Federico cruz", color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(text = "Hoy", color = Color.Gray, fontSize = 10.sp)
    }
}

@Composable
fun PendingTasksList(onTaskClick: () -> Unit) {
    val tasks = listOf("Reunión con Jose Miguel Villalobos", "Reunión con Juan Diego Castro", "Reunión con Francisco Dall'Anese")
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        tasks.forEach { title ->
            PendingTaskItem(title = title, onClick = onTaskClick)
        }
    }
}

@Composable
fun PendingTaskItem(title: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().border(1.dp, FigmaGold, RoundedCornerShape(8.dp)).clickable { onClick() }, shape = RoundedCornerShape(8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF171E27))) {
        Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.ic_task_check_hollow), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(28.dp).clickable { })
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "Caso 25-000044-033-PE", color = Color.White, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(painter = painterResource(id = R.drawable.ic_task_calendar_white), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(20.dp))
                Text(text = "11:00", color = Color.White, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(painter = painterResource(id = R.drawable.ic_arrow_right_gold), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(20.dp))
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

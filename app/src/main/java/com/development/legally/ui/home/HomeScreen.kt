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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.Nuevo.NewOverlay
import com.development.legally.ui.theme.*
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.ClasesSupremas.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.data.model.Case
import com.development.legally.data.model.Event
import com.development.legally.ui.agenda.AgendaViewModel
import com.development.legally.ui.cases.CasosViewModel
import com.development.legally.ui.clients.ClientViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToNewClient: () -> Unit = {},
    onNavigateToNewEvent: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToClients: () -> Unit = {},
    onNavigateToEditClient: (String) -> Unit = {},
    onNavigateToEditCase: (String) -> Unit = {},
    onNavigateToEditEvent: (String) -> Unit = {},
    agendaViewModel: AgendaViewModel = viewModel(),
    casosViewModel: CasosViewModel = viewModel(),
    clientViewModel: ClientViewModel = viewModel()
) {
    var showNewMenu by remember { mutableStateOf(false) }
    
    val agendaState by agendaViewModel.uiState.collectAsState()
    val casosState by casosViewModel.uiState.collectAsState()
    val clientsState by clientViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        agendaViewModel.loadEvents()
        casosViewModel.loadCasos()
        clientViewModel.loadClients()
    }

    // Lógica para "Qué hay para hoy" (Próximo evento)
    val nextEvent = remember(agendaState.events) {
        agendaState.events
            .filter { it.fechaHora != null && it.fechaHora!!.toDate().after(Date()) }
            .minByOrNull { it.fechaHora!!.toDate().time }
    }

    // Estadísticas reales
    val activeCasesCount = casosState.cases.count { it.status == "Activo" || it.status == "En proceso" }
    val todayAudienciasCount = agendaState.events.count { 
        it.tipo == "Audiencia" && isSameDay(it.fechaHora, Date()) 
    }
    val upcomingEventsCount = agendaState.events.count { 
        it.fechaHora != null && it.fechaHora!!.toDate().after(Date()) 
    }
    val totalClientsCount = clientsState.clients.size

    // Casos más recientes
    val recentCases = remember(casosState.cases) {
        casosState.cases
            .sortedByDescending { it.createdAt?.toDate()?.time ?: 0L }
            .take(3)
    }

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
                    NotificationAction(onNotificationClick = onNavigateToEditEvent)
                }
            },
            bottomBar = { 
                LegallyBottomNavigationBar(
                    currentRoute = "home",
                    onInicioClick = { 
                        agendaViewModel.loadEvents()
                        casosViewModel.loadCasos()
                        clientViewModel.loadClients()
                    },
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
                item { 
                    if (nextEvent != null) {
                        NextEventCard(event = nextEvent, onClick = { onNavigateToEditEvent(nextEvent.eventId) })
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .border(1.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay eventos próximos", color = Color.Gray)
                        }
                    }
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

                item { 
                    StatsRow(
                        activeCases = activeCasesCount,
                        todayAudiencias = todayAudienciasCount,
                        upcomingEvents = upcomingEventsCount,
                        totalClients = totalClientsCount,
                        onNavigateToCases = onNavigateToCases,
                        onNavigateToAgenda = onNavigateToAgenda,
                        onNavigateToClients = onNavigateToClients
                    ) 
                }
                
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
                        RecentCasesList(cases = recentCases, onCaseClick = onNavigateToEditCase)
                    }
                }
                
                item { Spacer(modifier = Modifier.height(100.dp)) }
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
fun NextEventCard(event: Event, onClick: () -> Unit) {
    val date = event.fechaHora?.toDate() ?: Date()
    val monthFormat = SimpleDateFormat("MMMM 'de' yyyy", Locale.getDefault())
    val dayNameFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val dayNumberFormat = SimpleDateFormat("dd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

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
                Text(text = monthFormat.format(date).replaceFirstChar { it.uppercase() }, color = Color.White, fontSize = 12.sp)
                Text(text = dayNameFormat.format(date).replaceFirstChar { it.uppercase() }, color = Color.White, fontSize = 15.sp)
                Text(text = dayNumberFormat.format(date), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            }
            Box(modifier = Modifier.width(1.dp).height(80.dp).background(Color.White.copy(alpha = 0.5f)))
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(text = timeFormat.format(date), color = Color.White, fontSize = 14.sp)
                Text(text = event.titulo, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "Caso: ${event.casoRelacionado}", color = Color.White, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.ic_location_pin), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = event.lugar.ifEmpty { "Lugar no definido" }.uppercase(), color = Color.White, fontSize = 10.sp, maxLines = 1)
                }
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.height(80.dp)) {
                Box(modifier = Modifier.drawBehind { drawRoundRect(color = Color(0xFF9E8D44), style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)), cornerRadius = CornerRadius(8.dp.toPx())) }.padding(horizontal = 10.dp, vertical = 4.dp), contentAlignment = Alignment.Center) {
                    Text(text = event.tipo, color = Color.White, fontSize = 11.sp)
                }
                Icon(painter = painterResource(id = R.drawable.ic_arrow_right_gold), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun StatsRow(
    activeCases: Int,
    todayAudiencias: Int,
    upcomingEvents: Int,
    totalClients: Int,
    onNavigateToCases: () -> Unit,
    onNavigateToAgenda: () -> Unit,
    onNavigateToClients: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatCard(modifier = Modifier.weight(1f), count = activeCases.toString(), labelLines = listOf("Expedien", "tes", "Activos"), icon = painterResource(id = R.drawable.ic_card_folder_figma), onClick = onNavigateToCases)
        StatCard(modifier = Modifier.weight(1f), count = todayAudiencias.toString(), labelLines = listOf("Audien", "cias", "Para: hoy"), icon = painterResource(id = R.drawable.ic_card_calendar_figma), onClick = onNavigateToAgenda)
        StatCard(modifier = Modifier.weight(1f), count = upcomingEvents.toString(), labelLines = listOf("Compromi", "sos", "Próximamente"), icon = painterResource(id = R.drawable.ic_card_clock_figma), onClick = onNavigateToAgenda)
        StatCard(modifier = Modifier.weight(1f), count = totalClients.toString(), labelLines = listOf("Clientes", "Regis", "trados"), icon = painterResource(id = R.drawable.ic_nav_clientes_on), onClick = onNavigateToClients)
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
fun RecentCasesList(cases: List<Case>, onCaseClick: (String) -> Unit) {
    if (cases.isEmpty()) {
        Text("No hay casos recientes", color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            cases.forEach { case ->
                RecentCaseItem(case = case, onClick = { onCaseClick(case.firestoreDocId) })
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
            }
        }
    }
}

@Composable
fun RecentCaseItem(case: Case, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(42.dp).background(FigmaGold.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Icon(painter = painterResource(id = R.drawable.ic_card_folder), contentDescription = null, tint = FigmaGold, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = case.caseNumber, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(text = case.CaseTittle, color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(text = if (isSameDay(case.createdAt, Date())) "Hoy" else SimpleDateFormat("dd MMM", Locale.getDefault()).format(case.createdAt?.toDate() ?: Date()), color = Color.Gray, fontSize = 10.sp)
    }
}

private fun isSameDay(timestamp: Timestamp?, date: Date): Boolean {
    if (timestamp == null) return false
    val cal1 = Calendar.getInstance().apply { time = timestamp.toDate() }
    val cal2 = Calendar.getInstance().apply { time = date }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

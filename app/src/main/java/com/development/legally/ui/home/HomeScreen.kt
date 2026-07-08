package com.development.legally.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.theme.*
import com.development.legally.ui.navigation.LegallyBottomNavigationBar

val FigmaBackground = Color(0xFF1C2632)
val FigmaGold = Color(0xFF9E8D44)
val FigmaSearchBackground = Color(0xFF171E27)
val FigmaNavBackground = Color(0xFF171E27)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToClients: () -> Unit = {}
) {
    var showNewMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = { HomeTopBar() },
            bottomBar = { 
                LegallyBottomNavigationBar(
                    currentRoute = "home",
                    onInicioClick = { /* Already here */ },
                    onExpedientesClick = onNavigateToCases,
                    onCrearClick = { showNewMenu = true },
                    onAgendaClick = onNavigateToAgenda,
                    onClientesClick = onNavigateToClients
                ) 
            },
            containerColor = FigmaBackground,
            modifier = Modifier
                .fillMaxSize()
                .then(if (showNewMenu) Modifier.blur(16.4.dp) else Modifier)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 17.dp, vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                item { 
                    Spacer(modifier = Modifier.height(16.dp)) 
                    SearchBar() 
                }
                item { SectionHeader("Que hay para hoy?") }
                item { StatsRow() }
                item { SectionHeader("Que sigue ahora?") }
                item { NextEventCard() }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Casos mas recientes",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
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
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pendientes:",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ver todos",
                            color = FigmaGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                item { PendingTasksList() }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }

        if (showNewMenu) {
            NewOverlay(
                onClose = { showNewMenu = false },
                onNewClient = { /* Handle */ showNewMenu = false },
                onNewEvent = { /* Handle */ showNewMenu = false },
                onNewCase = { 
                    showNewMenu = false
                    onNavigateToNewCase()
                }
            )
        }
    }
}

@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 17.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.boton_usuario_expedientes),
            contentDescription = "User Profile",
            tint = FigmaGold,
            modifier = Modifier
                .size(width = 15.dp, height = 19.dp)
                .border(0.5.dp, FigmaGold)
        )
        Icon(
            painter = painterResource(id = R.drawable.boton_notificaciones_expedientes),
            contentDescription = "Notifications",
            tint = FigmaGold,
            modifier = Modifier
                .size(width = 23.dp, height = 19.2.dp)
                .border(0.5.dp, FigmaGold)
        )
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(FigmaSearchBackground, RoundedCornerShape(44.dp))
            .border(1.dp, FigmaGold, RoundedCornerShape(44.dp))
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Buscar",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Search Icon",
            tint = FigmaGold,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun StatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(modifier = Modifier.weight(1f), count = "+1k", label = "Expedientes\nActivos")
        StatCard(modifier = Modifier.weight(1f), count = "x", label = "Audiencias", subLabel = "Para: hoy")
        StatCard(modifier = Modifier.weight(1f), count = "+1k", label = "Compromisos\nPróximamente", subLabel = "Próximamente")
        StatCard(modifier = Modifier.weight(1f), count = "+1k", label = "Tareas\nPendientes", subLabel = "Pendientes")
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, count: String, label: String, subLabel: String? = null) {
    Card(
        modifier = modifier.height(190.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, FigmaGold)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(23.dp)
                    .height(45.dp)
                    .background(FigmaGold)
            )
            Text(
                text = count,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = Color.White,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
            if (subLabel != null) {
                Text(
                    text = subLabel,
                    color = Color.White,
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(modifier = Modifier.height(9.dp))
            }
        }
    }
}

@Composable
fun NextEventCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, FigmaGold, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                Text(text = "Mayo de 2026", color = Color.Gray, fontSize = 10.sp)
                Text(text = "Martes", color = Color.White, fontSize = 14.sp)
                Text(text = "20", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }

            VerticalDivider(color = FigmaGold, modifier = Modifier.height(70.dp).padding(horizontal = 12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "hh:mm:a/p", color = Color.Gray, fontSize = 12.sp)
                    Box(
                        modifier = Modifier
                            .border(1.dp, FigmaGold, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = "Audiencia", color = Color.Gray, fontSize = 10.sp)
                    }
                }
                Text(text = "Lorem Ipsum", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(text = "Caso numero Lorem Ipsum", color = Color.White, fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = FigmaGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "HEARTBREAK AVENUE, TWICELAND", color = Color.Gray, fontSize = 10.sp)
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = FigmaGold,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun RecentCasesList() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RecentCaseItem("25-000044-033-PE", "Christian Bullgarelli vs Federico cruz A.K.A", "Activo", "Hoy")
        HorizontalDivider(color = Color.White.copy(alpha = 0.15f), thickness = 1.dp)
        RecentCaseItem("20-000115-1218-PE", "Procuradoria vs Luis Guillermo Solis Rivera", "Activo", "Ayer")
        HorizontalDivider(color = Color.White.copy(alpha = 0.15f), thickness = 1.dp)
        RecentCaseItem("25-002920-0175-PE", "Rodrigo Arias Sanchez vs STEPHAN", "Activo", "25/05")
    }
}

@Composable
fun RecentCaseItem(id: String, description: String, status: String, update: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = id, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text(text = description, color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = status, color = Color.Gray, fontSize = 10.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = FigmaGold,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(text = "Actualizado: $update", color = Color.Gray, fontSize = 10.sp)
        }
    }
}

@Composable
fun PendingTasksList() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PendingTaskItem("Reunion con Jose Miguel Villalobos Umaña", "Caso 25-000044-033-PE", "Hoy, 11:00")
        HorizontalDivider(color = Color.White.copy(alpha = 0.15f), thickness = 1.dp)
        PendingTaskItem("Reunion con Juan Diego Castro Fernandez", "Caso 25-000044-033-PE", "Mañana, 11:00")
    }
}

@Composable
fun PendingTaskItem(title: String, subtitle: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .border(1.dp, FigmaGold, CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = Color.Gray, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Icon(
                painter = painterResource(id = R.drawable.agenda),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = time, color = Color.White, fontSize = 10.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = FigmaGold,
                    modifier = Modifier.size(20.dp)
                )
            }
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

package com.development.legally.ui.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.components.*

private val FigmaBackground = Color(0xFF1C2632)
private val FigmaGold = Color(0xFF9E8D44)
private val FigmaSearchBackground = Color(0xFF171E27)
private val FigmaButtonGray = Color(0xFF485A70)
private val FigmaUrgentRed = Color(0xFFF50505)
private val FigmaStatusNormal = Color(0xFFFDBB47)
private val FigmaStatusAvailable = Color(0xFF00FF11)
private val FigmaStatusBusy = Color(0xFFFF0000)

@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToClients: () -> Unit = {}
) {
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
                // 1. Usuario (Logout)
                UserAction(onLogoutConfirm = onLogout)
                
                // 4. Titulo Sección
                SectionHeader(title = "Agenda del abogado", modifier = Modifier.weight(1f))
                
                // 2. Notificaciones
                NotificationAction()
            }
        },
        bottomBar = {
            LegallyBottomNavigationBar(
                currentRoute = "agenda",
                onInicioClick = onNavigateToHome,
                onExpedientesClick = onNavigateToCases,
                onCrearClick = onNavigateToNewCase,
                onAgendaClick = { /* Ya estamos aquí */ },
                onClientesClick = onNavigateToClients
            )
        },
        containerColor = FigmaBackground
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 17.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 3. Barra de búsqueda unificada
            MainSearchBar(
                title = "Buscar eventos, fechas...",
                onSearch = { /* Implementar búsqueda */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Filtros locales
            LocalFilterSection(title = "Filtrar por")

            Spacer(modifier = Modifier.height(24.dp))

            // Ordenar locales
            LocalFilterSection(title = "Ordenar por")

            Spacer(modifier = Modifier.height(24.dp))

            // Listado de agenda
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(FigmaSearchBackground)
            ) {
                AgendaList()
            }
        }
    }
}

@Composable
private fun LocalFilterSection(title: String) {
    Column {
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W900
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterButton(text = "Fecha", modifier = Modifier.weight(1f))
            FilterButton(text = "Cliente", modifier = Modifier.weight(1f))
            FilterButton(text = "Tipo", modifier = Modifier.weight(1f))
            FilterButton(text = "Urgencia", color = FigmaUrgentRed, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun FilterButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = FigmaButtonGray
) {
    Box(
        modifier = modifier
            .height(26.dp)
            .background(color, RoundedCornerShape(2.dp))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.W900
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(10.dp)
            )
        }
    }
}

@Composable
fun AgendaList(modifier: Modifier = Modifier) {
    val items = listOf(
        AgendaItemData("08:00 - 09:00", "Exp. 25-000444-033-PE", "Christian Bulgarelli vs Rodrigo Chaves", "Ocupado", FigmaStatusBusy),
        AgendaItemData("10:30 - 11:30", "Exp. 24-001234-004-CI", "Maria Perez vs Banco Central", "Disponible", FigmaStatusAvailable),
        AgendaItemData("13:00 - 14:00", "Exp. 23-005566-012-FA", "Juan Soto vs Ana Mendez", "Normal", FigmaStatusNormal),
        AgendaItemData("15:00 - 16:30", "Exp. 25-000987-045-PE", "Estado vs Luis Gomez", "Ocupado", FigmaStatusBusy),
        AgendaItemData("17:00 - 18:00", "Exp. 25-001122-099-CO", "Telecom S.A. vs Gobierno", "Disponible", FigmaStatusAvailable),
        AgendaItemData("18:30 - 19:30", "Exp. 24-008899-011-PE", "Ministerio vs Desconocido", "Normal", FigmaStatusNormal)
    )
    
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(items) { item ->
            AgendaListItem(item)
        }
    }
}

@Composable
fun AgendaListItem(
    data: AgendaItemData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = FigmaGold,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = data.duration,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(100.dp)
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.caseId,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = data.client,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(data.statusColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = data.status,
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Icon(
                painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

data class AgendaItemData(
    val duration: String,
    val caseId: String,
    val client: String,
    val status: String,
    val statusColor: Color
)

@Preview(showBackground = true, widthDp = 389, heightDp = 879)
@Composable
fun AgendaScreenPreview() {
    LegallyTheme {
        AgendaScreen()
    }
}

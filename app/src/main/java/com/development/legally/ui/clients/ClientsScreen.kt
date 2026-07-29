package com.development.legally.ui.clients

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.Client
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.components.*

private val FigmaBackground = Color(0xFF1C2632)
private val FigmaGold = Color(0xFF9E8D44)
private val FigmaSearchBackground = Color(0xFF171E27)
private val FigmaButtonGray = Color(0xFF485A70)
private val FigmaUrgentRed = Color(0xFFF50505)
private val FigmaStatusActive = Color(0xFF00FF11)
private val FigmaStatusResolved = Color(0xFFFF0000)

@Composable
fun ClientsScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToEditClient: (String) -> Unit = {},
    onNavigateToClientDetail: (String) -> Unit = {},
    viewModel: ClientViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
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
                Button(
                    onClick = { onNavigateToEditClient("new") },
                    modifier = Modifier
                        .height(36.dp)
                        .width(36.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FigmaGold),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+", fontSize = 24.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                NotificationAction()
            }
        },
        bottomBar = {
            LegallyBottomNavigationBar(
                currentRoute = "clients",
                onInicioClick = onNavigateToHome,
                onExpedientesClick = onNavigateToCases,
                onCrearClick = onNavigateToNewCase,
                onAgendaClick = onNavigateToAgenda,
                onClientesClick = {}
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
            
            MainSearchBar(
                title = "Buscar clientes...",
                onSearch = { query -> viewModel.searchClients(query) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LocalSectionHeader(title = "Filtrar por:")

            Spacer(modifier = Modifier.height(24.dp))

            LocalSectionHeader(title = "Ordenar por:")

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(FigmaSearchBackground)
            ) {
                if (uiState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = FigmaGold
                    )
                } else if (uiState.error != null) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadClients() },
                            colors = ButtonDefaults.buttonColors(containerColor = FigmaGold)
                        ) {
                            Text("Reintentar")
                        }
                    }
                } else if (uiState.filtered.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (uiState.searchQuery.isBlank()) 
                                "No hay clientes registrados" 
                            else 
                                "No se encontraron clientes",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.filtered) { client ->
                            ClientListItem(
                                client = client,
                                onEditClick = { onNavigateToEditClient(client.id) },
                                onDetailClick = { onNavigateToClientDetail(client.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocalSectionHeader(title: String) {
    Column {
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
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
private fun FilterButton(
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
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp).rotate(90f)
            )
        }
    }
}

@Composable
private fun ClientListItem(
    client: Client,
    onEditClick: () -> Unit,
    onDetailClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = FigmaGold,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .clickable { onDetailClick() }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEditClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${client.name} ${client.lastName}",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = client.email,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 24.dp)
                    .background(
                        FigmaStatusActive,
                        RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = client.personType.ifEmpty { "Física" },
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Icon(
                painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
                contentDescription = null,
                tint = FigmaGold,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private data class ClientItemData(
    val id: String,
    val name: String,
    val activeFiles: String,
    val processCount: String,
    val isActive: Boolean
)

@Preview(showBackground = true, widthDp = 389, heightDp = 879)
@Composable
fun ClientsScreenPreview() {
    LegallyTheme {
        ClientsScreen()
    }
}

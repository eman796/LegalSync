package com.development.legally.ui.clients

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.development.legally.R
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.navigation.LegallyBottomNavigationBar

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
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToEditClient: (String) -> Unit = {}
) {
    Scaffold(
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
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 2. Usuario
            Icon(
                painter = painterResource(id = R.drawable.boton_usuario_expedientes),
                contentDescription = null,
                tint = FigmaGold,
                modifier = Modifier
                    .offset(x = 17.dp, y = 32.dp)
                    .size(width = 15.dp, height = 15.dp)
            )

            // 3. Titulo Clientes del abogado
            Text(
                text = "Clientes del abogad@",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(x = 0.dp, y = 33.dp)
                    .fillMaxWidth()
                    .height(19.dp)
            )

            // 4. Notificaciones Clientes
            Icon(
                painter = painterResource(id = R.drawable.boton_notificaciones_expedientes),
                contentDescription = null,
                tint = FigmaGold,
                modifier = Modifier
                    .offset(x = 354.dp, y = 32.dp)
                    .size(width = 23.dp, height = 19.2.dp)
            )

            // 5. Barra de busqueda Clientes
            Box(
                modifier = Modifier
                    .offset(x = 17.dp, y = 67.dp)
                    .size(width = 356.dp, height = 48.dp)
                    .background(FigmaSearchBackground, RoundedCornerShape(44.dp))
                    .border(1.dp, FigmaGold, RoundedCornerShape(44.dp))
            ) {
                Text(
                    text = "Buscar expedientes, clientes o casos...",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterStart)
                )
                
                // 5.1 Cancelar Barra de busqueda Clientes
                Box(
                    modifier = Modifier
                        .offset(x = 305.dp, y = 6.dp)
                        .size(width = 31.dp, height = 36.dp)
                        .background(FigmaGold)
                        .clickable { }
                )
            }

            // 6. Filtrar por (general)
            SectionHeader(
                title = "Filtrar por:",
                modifier = Modifier.offset(x = 17.dp, y = 124.dp)
            )

            // 7. Ordenar por (general)
            SectionHeader(
                title = "Ordenar por:",
                modifier = Modifier.offset(x = 17.dp, y = 172.dp)
            )

            // 8. Listado de Clientes general
            Box(
                modifier = Modifier
                    .offset(x = 17.dp, y = 220.dp)
                    .size(width = 360.dp, height = 591.dp)
                    .background(FigmaSearchBackground)
            ) {

                val sampleClients = listOf(
                    ClientItemData("1", "Juan Perez", "Expedientes activos: 3", "12 Procesos", true),
                    ClientItemData("2", "Maria Garcia", "Expedientes activos: 1", "5 Procesos", false),
                    ClientItemData("3", "Carlos Rodriguez", "Expedientes activos: 0", "8 Procesos", true),
                    ClientItemData("4", "Ana Martinez", "Expedientes activos: 2", "10 Procesos", false),
                    ClientItemData("5", "Luis Hernandez", "Expedientes activos: 4", "15 Procesos", true),
                    ClientItemData("6", "Sofia Lopez", "Expedientes activos: 1", "3 Procesos", true),
                    ClientItemData("7", "Diego Ruiz", "Expedientes activos: 0", "2 Procesos", false))

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(sampleClients) { client ->
                        ClientListItem(
                            data = client,
                            onEditClick = { onNavigateToEditClient(client.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 358.dp, height = 50.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.offset(x = 0.dp, y = 0.dp)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
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
    data: ClientItemData,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = FigmaGold,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = FigmaGold,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        // 8.2 Nombre Cliente
        Text(
            text = data.name,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .offset(x = 28.35.dp, y = 0.dp)
                .size(width = 111.28.dp, height = 23.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 8.3 Expedientes activos
        Text(
            text = data.activeFiles,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .offset(x = 28.35.dp, y = 23.dp)
                .size(width = 114.43.dp, height = 15.dp),
            maxLines = 1
        )

        // 8.4 Cantidad Procesos
        Text(
            text = data.processCount,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .offset(x = 29.4.dp, y = 38.dp)
                .size(width = 328.6.dp, height = 8.dp),
            maxLines = 1
        )

        // 8.5 Etiqueta de actividad
        Box(
            modifier = Modifier
                .offset(x = 288.82.dp, y = 18.dp) // Adjusted slightly to fit within element
                .size(width = 47.24.dp, height = 23.dp)
                .background(
                    if (data.isActive) FigmaStatusActive else FigmaStatusResolved,
                    RoundedCornerShape(2.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (data.isActive) "Activo" else "Resuelto",
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
        
        // 8.6 Ir a
        Icon(
            painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
            contentDescription = "Editar Cliente",
            tint = FigmaGold,
            modifier = Modifier
                .offset(x = 342.2.dp, y = 14.46.dp)
                .size(width = 8.49.dp, height = 17.07.dp)
                .clickable { onEditClick() }
        )
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

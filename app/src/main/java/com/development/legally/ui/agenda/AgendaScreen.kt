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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 389.dp, height = 879.dp)
            .background(FigmaBackground)
    ) {
        // 2. Usuario Agenda
        Icon(
            painter = painterResource(id = R.drawable.boton_usuario_expedientes),
            contentDescription = null,
            tint = FigmaGold,
            modifier = Modifier
                .offset(x = 17.dp, y = 32.dp)
                .size(15.dp)
        )

        // 3. Titulo Agenda del abogado
        Text(
            text = "Agenda del abogado",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(x = 0.dp, y = 33.dp)
                .width(389.dp)
                .height(19.dp)
        )

        // 4. Notificaciones Agenda
        Box(
            modifier = Modifier
                .offset(x = 354.dp, y = 32.dp)
                .size(width = 23.dp, height = 19.2.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.boton_notificaciones_expedientes),
                contentDescription = null,
                tint = FigmaGold,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 5. Barra de busqueda agenda
        Box(
            modifier = Modifier
                .offset(x = 18.dp, y = 67.dp)
                .size(width = 357.dp, height = 48.dp)
                .background(FigmaSearchBackground, RoundedCornerShape(44.dp))
                .border(1.dp, FigmaGold, RoundedCornerShape(44.dp))
        ) {
            Text(
                text = "Buscar",
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .align(Alignment.CenterStart)
            )
            
            // 5.1 Cancelar Barra de busqueda Agenda
            Box(
                modifier = Modifier
                    .offset(x = 304.dp, y = 6.dp)
                    .size(width = 31.dp, height = 36.dp)
                    .background(FigmaGold)
                    .clickable { }
            )
        }

        // 6. Filtrar por (general)
        Box(
            modifier = Modifier
                .offset(x = 17.dp, y = 124.dp)
                .size(width = 358.dp, height = 47.dp)
        ) {
            Text(
                text = "Filtrar por",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.W900,
                modifier = Modifier.align(Alignment.TopStart)
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

        // 7. Ordenar por (general)
        Box(
            modifier = Modifier
                .offset(x = 17.dp, y = 180.dp) // Adjusted slightly to Y180 to fit content below Y172 title
                .size(width = 358.dp, height = 47.dp)
        ) {
            Text(
                text = "Ordenar por",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.W900,
                modifier = Modifier.align(Alignment.TopStart)
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

        // 8. Listado de agenda general
        Box(
            modifier = Modifier
                .offset(x = 15.dp, y = 240.dp) // Adjusted Y for spacing
                .size(width = 360.dp, height = 591.dp)
                .background(FigmaSearchBackground)
        ) {
            AgendaList()
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
            .height(49.dp)
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
        // 8.2 Duracion: X3, Y5
        Text(
            text = data.duration,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.W900,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(x = 3.dp, y = 5.dp)
                .size(width = 78.dp, height = 34.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        // 8.4 Cliente relacionado: X164, Y15
        Text(
            text = data.client,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(x = 164.dp, y = 15.dp)
                .size(width = 176.dp, height = 11.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // 8.3 caso relacionado: X2, Y24
        Text(
            text = data.caseId,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(x = 2.dp, y = 24.dp)
                .size(width = 176.dp, height = 23.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        // 8.5 Mostrar como: bola (X263, Y24)
        Box(
            modifier = Modifier
                .offset(x = 263.dp, y = 24.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(data.statusColor)
        )
        
        // 8.5 Mostrar como: texto (X290, Y24)
        Text(
            text = data.status,
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .offset(x = 280.dp, y = 24.dp)
                .size(width = 48.dp, height = 14.dp)
        )
        
        // 8.6 Ir a: X341, Y18
        Icon(
            painter = painterResource(id = R.drawable.boton_ir_a_lista_expedientes_expedientes),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .offset(x = 341.dp, y = 18.dp)
                .size(width = 8.49.dp, height = 17.07.dp)
        )
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

package com.development.legally.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R

private val FigmaGold = Color(0xFF9E8D44)
private val FigmaNavBackground = Color(0xFF171E27)
private val GrayBorder = Color(0xFF485A70)
private val NavLabelWhite = Color(0xFFFFFFFF)

@Composable
fun LegallyBottomNavigationBar(
    currentRoute: String,
    onInicioClick: () -> Unit = {},
    onExpedientesClick: () -> Unit = {},
    onCrearClick: () -> Unit = {},
    onAgendaClick: () -> Unit = {},
    onClientesClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(Color.Transparent)
    ) {
        // Main bar background with top gold border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(FigmaNavBackground)
                .drawBehind {
                    drawLine(
                        color = FigmaGold,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavItem(
                icon = R.drawable.boton_inicio_expedientes,
                label = "Inicio",
                isSelected = currentRoute == "home",
                onClick = onInicioClick
            )
            NavItem(
                icon = R.drawable.boton_expedientes_expedientes,
                label = "Expedientes",
                isSelected = currentRoute == "cases",
                onClick = onExpedientesClick
            )
            
            // Placeholder for the middle button to maintain spacing
            Spacer(modifier = Modifier.width(60.dp))

            NavItem(
                icon = R.drawable.boton_agendaexpedientes,
                label = "Agenda",
                isSelected = currentRoute == "agenda",
                onClick = onAgendaClick
            )
            NavItem(
                icon = R.drawable.boton_clientes_expedientes,
                label = "Clientes",
                isSelected = currentRoute == "clients",
                onClick = onClientesClick
            )
        }

        // Floating "Crear" button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .offset(y = (-15).dp)
                .clickable { onCrearClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(FigmaNavBackground)
                    .border(1.5.dp, FigmaGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear",
                    tint = FigmaGold,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .border(0.8.dp, FigmaGold, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "Crear",
                    color = FigmaGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = FigmaGold,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .border(
                    width = 0.8.dp,
                    color = if (isSelected) FigmaGold else GrayBorder,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 1.dp)
        ) {
            Text(
                text = label,
                color = if (isSelected) FigmaGold else NavLabelWhite,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

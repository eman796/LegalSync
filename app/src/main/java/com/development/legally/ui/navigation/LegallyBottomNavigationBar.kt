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
            .height(105.dp)
            .background(Color.Transparent)
    ) {
        // Cuerpo de la barra
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
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
                .height(75.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavItem(
                iconOn = R.drawable.ic_nav_inicio_on,
                iconOff = R.drawable.ic_nav_inicio_off,
                label = "Inicio",
                isSelected = currentRoute == "home",
                onClick = onInicioClick
            )
            NavItem(
                iconOn = R.drawable.ic_nav_expedientes_on,
                iconOff = R.drawable.ic_nav_expedientes_off,
                label = "Expedientes",
                isSelected = currentRoute == "cases",
                onClick = onExpedientesClick
            )
            
            Spacer(modifier = Modifier.width(50.dp))

            NavItem(
                iconOn = R.drawable.ic_nav_agenda_on,
                iconOff = R.drawable.ic_nav_agenda_off,
                label = "Agenda",
                isSelected = currentRoute == "agenda",
                onClick = onAgendaClick
            )
            NavItem(
                iconOn = R.drawable.ic_nav_clientes_on,
                iconOff = R.drawable.ic_nav_clientes_off,
                label = "Clientes",
                isSelected = currentRoute == "clients",
                onClick = onClientesClick
            )
        }

        // Botón Crear (Central)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .offset(y = (-10).dp)
                .clickable { onCrearClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(FigmaNavBackground)
                    .border(2.dp, FigmaGold, CircleShape),
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
                    .background(FigmaGold, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 2.dp)
            ) {
                Text(text = "Crear", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun NavItem(
    iconOn: Int,
    iconOff: Int,
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
        // Icono On/Off
        Icon(
            painter = painterResource(id = if (isSelected) iconOn else iconOff),
            contentDescription = label,
            tint = Color.Unspecified, 
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        // Diseño de la pastilla (Pill)
        Box(
            modifier = Modifier
                .background(
                    if (isSelected) FigmaGold else Color.Transparent,
                    RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = FigmaGold,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 10.dp, vertical = 2.dp)
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

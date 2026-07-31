package com.development.legally.ui.Nuevo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R

private val OverlayGold = Color(0xFF9E8D44)
private val OverlayBackground = Color(0xFF1C2632)
private val CancelRed = Color(0xFFE53935)

@Composable
fun NewOverlay(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    onNewClient: () -> Unit = {},
    onNewEvent: () -> Unit = {},
    onNewCase: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(OverlayBackground.copy(alpha = 0.95f))
            .clickable(onClick = onClose)
    ) {
        // Líneas de conexión
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height - 100.dp.toPx()

            drawLine(color = OverlayGold, start = Offset(centerX, centerY - 40.dp.toPx()), end = Offset(centerX, centerY - 130.dp.toPx()), strokeWidth = 2.dp.toPx())
            drawLine(color = OverlayGold, start = Offset(centerX + 30.dp.toPx(), centerY - 35.dp.toPx()), end = Offset(centerX + 100.dp.toPx(), centerY - 75.dp.toPx()), strokeWidth = 2.dp.toPx())
            drawLine(color = OverlayGold, start = Offset(centerX - 30.dp.toPx(), centerY - 35.dp.toPx()), end = Offset(centerX - 100.dp.toPx(), centerY - 75.dp.toPx()), strokeWidth = 2.dp.toPx())
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // NUEVO EVENTO
            NewActionItem(
                icon = { Icon(painterResource(id = R.drawable.ic_tarjeta_audiencias), contentDescription = null, tint = OverlayGold, modifier = Modifier.size(32.dp)) },
                label = "Evento",
                modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-185).dp),
                onClick = onNewEvent
            )

            // NUEVO CLIENTE (BOTÓN AÑADIR SOLICITADO)
            NewActionItem(
                icon = {
                    Box(
                        modifier = Modifier.size(40.dp).background(OverlayGold, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(28.dp))
                    }
                },
                label = "Cliente",
                modifier = Modifier.align(Alignment.BottomCenter).offset(x = 130.dp, y = (-105).dp),
                onClick = onNewClient
            )

            // NUEVO EXPEDIENTE
            NewActionItem(
                icon = { Icon(painterResource(id = R.drawable.ic_tarjeta_expedientes), contentDescription = null, tint = OverlayGold, modifier = Modifier.size(32.dp)) },
                label = "Expediente",
                modifier = Modifier.align(Alignment.BottomCenter).offset(x = (-130).dp, y = (-105).dp),
                onClick = onNewCase
            )

            // BOTÓN CERRAR
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 60.dp)
                    .size(64.dp)
                    .background(CancelRed, CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White, modifier = Modifier.size(36.dp))
            }
        }
    }
}

@Composable
private fun NewActionItem(
    icon: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = "NUEVO", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.size(64.dp).clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

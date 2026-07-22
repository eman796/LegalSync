package com.development.legally.ui.Nuevo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.theme.LegallyTheme

// Using specific colors to match Figma description
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
        // Connecting Lines drawn using Canvas for precise positioning
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height - 110.dp.toPx()

            // Line to Evento (Up, 90 deg)
            drawLine(
                color = OverlayGold,
                start = Offset(centerX, centerY - 48.dp.toPx()),
                end = Offset(centerX, centerY - 120.dp.toPx()),
                strokeWidth = 2.dp.toPx()
            )

            // Line to Cliente (Right, -14.56 deg relative to some axis, but following visually)
            drawLine(
                color = OverlayGold,
                start = Offset(centerX + 32.dp.toPx(), centerY - 40.dp.toPx()),
                end = Offset(centerX + 90.dp.toPx(), centerY - 65.dp.toPx()),
                strokeWidth = 2.dp.toPx()
            )

            // Line to Caso (Left, 15 deg)
            drawLine(
                color = OverlayGold,
                start = Offset(centerX - 32.dp.toPx(), centerY - 40.dp.toPx()),
                end = Offset(centerX - 90.dp.toPx(), centerY - 65.dp.toPx()),
                strokeWidth = 2.dp.toPx()
            )
        }

        // Action Items Container
        Box(modifier = Modifier.fillMaxSize()) {
            // Nuevo Evento (Center Top)
            NewActionItem(
                iconRes = R.drawable.nuevo_evento,
                label = stringResource(id = R.string.event_label),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-175).dp),
                onClick = onNewEvent
            )

            // Nuevo Cliente (Right)
            NewActionItem(
                iconRes = R.drawable.nuevo_cliente,
                label = stringResource(id = R.string.client_label),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(x = 120.dp, y = (-100).dp),
                onClick = onNewClient
            )

            // Nuevo Caso (Left)
            NewActionItem(
                iconRes = R.drawable.nuevo_caso,
                label = stringResource(id = R.string.case_label),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(x = (-120).dp, y = (-100).dp),
                onClick = onNewCase
            )

            // Close Button (Center Bottom)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 60.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(CancelRed, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable(onClick = onClose),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close_label),
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.close_label),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NewActionItem(
    iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.new_label),
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.Transparent)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = OverlayGold,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, widthDp = 389, heightDp = 879)
@Composable
fun NewOverlayPreview() {
    LegallyTheme {
        NewOverlay()
    }
}

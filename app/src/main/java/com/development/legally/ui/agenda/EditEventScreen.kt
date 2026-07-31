package com.development.legally.ui.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.theme.FigmaBackground
import com.development.legally.ui.theme.FigmaGold
import com.development.legally.ui.theme.LegallyTheme

private val FigmaFieldBackground = Color(0xFF171E27)
private val FigmaTextWhite = Color(0xFFFFFFFF)

@Composable
fun EditEventScreen(
    eventId: String?,
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FigmaBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Barra Superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = FigmaGold,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() }
                )

                Text(
                    text = "Editar Evento",
                    color = FigmaTextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                )

                Text(
                    text = stringResource(id = R.string.cancel),
                    color = FigmaGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .width(82.dp)
                        .clickable { onCancelClick() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título del evento
            InputField(
                label = "Título del Evento",
                value = "Audiencia de Pruebas",
                fieldWidth = 355.dp
            )

            Spacer(modifier = Modifier.height(19.dp))

            // Expediente asociado
            InputField(
                label = "Expediente",
                value = "25-000044-033-PE",
                fieldWidth = 355.dp
            )

            Spacer(modifier = Modifier.height(19.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                InputField(
                    label = "Fecha",
                    value = "20/05/2026",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(38.dp))
                InputField(
                    label = "Hora",
                    value = "10:30 AM",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            InputField(
                label = "Notas",
                value = "Preparar los documentos de la defensa...",
                fieldHeight = 200.dp,
                isMultiline = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Boton Guardar Cambios
            Button(
                onClick = { onSaveClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FigmaGold),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "GUARDAR CAMBIOS",
                    color = FigmaTextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    fieldWidth: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.Dp.Unspecified,
    fieldHeight: androidx.compose.ui.unit.Dp = 50.dp,
    isMultiline: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = FigmaTextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .then(if (fieldWidth != androidx.compose.ui.unit.Dp.Unspecified) Modifier.width(fieldWidth) else Modifier.fillMaxWidth())
                .height(fieldHeight)
                .background(FigmaFieldBackground, RoundedCornerShape(12.dp))
                .border(1.dp, FigmaGold, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp),
            contentAlignment = if (isMultiline) Alignment.TopStart else Alignment.CenterStart
        ) {
            Text(
                text = value,
                color = FigmaTextWhite,
                fontSize = 15.sp,
                modifier = if (isMultiline) Modifier.padding(top = 12.dp) else Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditEventScreenPreview() {
    LegallyTheme {
        EditEventScreen(eventId = "event_123")
    }
}

package com.development.legally.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun OutlinedText(
    text: String,
    mainColor: Color,      // 1. Color principal (relleno)
    outlineColor: Color,   // 2. Color del borde (contorno)
    modifier: Modifier = Modifier,
    strokeWidth: Float = 2f,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    Box(modifier = modifier) {
        // Texto de fondo (el borde/contorno)
        Text(
            text = text,
            textAlign = textAlign,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = outlineColor,
                drawStyle = Stroke(
                    width = strokeWidth,
                    join = StrokeJoin.Round
                )
            )
        )
        // Texto de frente (el relleno principal)
        Text(
            text = text,
            textAlign = textAlign,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = mainColor
            )
        )
    }
}

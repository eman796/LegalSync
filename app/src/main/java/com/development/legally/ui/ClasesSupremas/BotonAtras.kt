package com.development.legally.ui.ClasesSupremas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
//Este es el tipico "Boton atras". Se usa como clase para no estar repitiendo codigo, dado que se usa en casi todas las pantallas de la aplicacion. Nada especial.
@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    goldColor: Color = Color(0xFF9E8D44)
) {
    Box(
        modifier = modifier
            .padding(start = 25.dp, top = 47.dp)
            .size(40.dp)
            .background(goldColor.copy(alpha = 0.2f), CircleShape)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Volver",
            tint = goldColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

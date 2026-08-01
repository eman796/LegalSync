package com.development.legally.ui.ClasesSupremas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.theme.FigmaBackground
import com.development.legally.ui.theme.FigmaGold
import com.development.legally.ui.theme.LegallyTheme

/**
 * CLASE SUPREMA DE EDICIÓN
 * Contiene la estructura y componentes base para todas las pantallas de edición.
 */
object EdicionSuprema {

    @Composable
    fun PantallaBase(
        titulo: String,
        textoBotonGuardar: String,
        textoBotonEliminar: String = "Eliminar",
        onAtras: () -> Unit,
        onCancelar: () -> Unit,
        onGuardar: () -> Unit,
        onDuplicar: () -> Unit = {},
        onEliminar: () -> Unit = {},
        contenido: @Composable ColumnScope.() -> Unit
    ) {
        var mostrarDialogoCancelar by remember { mutableStateOf(false) }

        if (mostrarDialogoCancelar) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoCancelar = false },
                containerColor = Color(0xFF1C2632),
                titleContentColor = Color.White,
                textContentColor = Color.White,
                title = { Text("¿Desea cancelar?") },
                text = { Text("Se eliminarán todos los datos ya creados y se devolverá a la pantalla anterior. ¿Estás seguro?") },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarDialogoCancelar = false
                        onCancelar()
                    }) {
                        Text("SÍ, CANCELAR", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoCancelar = false }) {
                        Text("NO", color = FigmaGold)
                    }
                }
            )
        }

        Surface(modifier = Modifier.fillMaxSize(), color = FigmaBackground) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(32.dp))

                // BARRA SUPERIOR (Flecha, Título, Duplicar, Cancelar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Volver",
                        tint = FigmaGold,
                        modifier = Modifier.size(28.dp).clickable { onAtras() }
                    )
                    Text(
                        text = titulo,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f).padding(start = 12.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Duplicar",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onDuplicar() }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Cancelar",
                        color = FigmaGold,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { mostrarDialogoCancelar = true }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CONTENIDO SCROLLEABLE
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 18.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    contenido()

                    Spacer(modifier = Modifier.height(32.dp))

                    // BOTÓN GUARDAR (Dinámico)
                    Button(
                        onClick = onGuardar,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FigmaGold),
                        shape = RoundedCornerShape(26.dp)
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_save_edit), contentDescription = null, modifier = Modifier.size(20.dp), tint = FigmaBackground)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = textoBotonGuardar, fontWeight = FontWeight.Bold, color = FigmaBackground, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // BOTÓN ELIMINAR (Dinámico)
                    Button(
                        onClick = onEliminar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .border(1.dp, Color(0xFFC64141), RoundedCornerShape(26.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A2525).copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(26.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFFC64141))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = textoBotonEliminar, fontWeight = FontWeight.Bold, color = Color(0xFFC64141), fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }

    @Composable
    fun TituloSeccion(titulo: String, logo: Int? = null) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
            if (logo != null) {
                Icon(
                    painter = painterResource(id = logo),
                    contentDescription = null,
                    tint = FigmaGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(11.dp))
            }
            Text(text = titulo, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }

    @Composable
    fun ElementoEdicion(
        titulo: String,
        placeholder: String,
        tipo: TipoDato = TipoDato.STRING,
        valor: String,
        onValorChange: (String) -> Unit,
        posX: Dp = 0.dp,
        posY: Dp = 0.dp,
        width: Dp = Dp.Unspecified,
        height: Dp = 50.dp,
        leadingIcon: (@Composable () -> Unit)? = null,
        maxChars: Int? = null,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .padding(start = posX, top = posY)
                .then(if (width != Dp.Unspecified) Modifier.width(width) else Modifier.fillMaxWidth())
        ) {
            Text(text = titulo, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            if (tipo == TipoDato.LISTA) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .background(Color(0xFF171E27), RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .clickable { }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (leadingIcon != null) {
                                leadingIcon()
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = if (valor.isEmpty()) placeholder else valor,
                                color = Color.White.copy(alpha = if (valor.isEmpty()) 0.5f else 1f),
                                fontSize = 14.sp
                            )
                        }
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_right_gold), contentDescription = null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                    }
                }
            } else {
                Column {
                    OutlinedTextField(
                        value = valor,
                        onValueChange = { if (maxChars == null || it.length <= maxChars) onValorChange(it) },
                        modifier = Modifier.fillMaxWidth().height(height),
                        placeholder = { Text(text = placeholder, color = Color.Gray.copy(alpha = 0.5f), fontSize = 14.sp) },
                        leadingIcon = leadingIcon,
                        keyboardOptions = KeyboardOptions(keyboardType = if (tipo == TipoDato.ENTERO) KeyboardType.Number else KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF171E27),
                            unfocusedContainerColor = Color(0xFF171E27),
                            focusedBorderColor = FigmaGold,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = height < 80.dp
                    )
                    if (maxChars != null) {
                        Text(
                            text = "Caracteres: ${valor.length} de $maxChars",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }

    enum class TipoDato { STRING, ENTERO, LISTA }
}

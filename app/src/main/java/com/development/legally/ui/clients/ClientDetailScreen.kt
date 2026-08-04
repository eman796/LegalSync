package com.development.legally.ui.clients

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.development.legally.R
import com.development.legally.data.model.Client
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.navigation.LegallyBottomNavigationBar

private val FigmaBackground = Color(0xFF1C2632)
private val FigmaGold = Color(0xFF9E8D44)
private val FigmaFieldBackground = Color(0xFF171E27)
private val FigmaStatusActive = Color(0xFF00FF11)

@Composable
fun ClientDetailScreen(
    clientId: String?,
    modifier: Modifier = Modifier,
    viewModel: ClientViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCases: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToClients: () -> Unit = {},
    onNavigateToEdit: (String) -> Unit = {}
) {
    var client by remember { mutableStateOf<Client?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(clientId) {
        if (clientId != null && clientId.isNotEmpty()) {
            viewModel.loadClientById(clientId) { loadedClient ->
                client = loadedClient
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 17.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = FigmaGold,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onNavigateBack() }
                )
                Text(
                    text = "Detalle del Cliente",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.boton_crear_expedientes),
                    contentDescription = "Editar",
                    tint = FigmaGold,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { clientId?.let { onNavigateToEdit(it) } }
                )
            }
        },
        bottomBar = {
            LegallyBottomNavigationBar(
                currentRoute = "clients",
                onInicioClick = onNavigateToHome,
                onExpedientesClick = onNavigateToCases,
                onCrearClick = onNavigateToNewCase,
                onAgendaClick = onNavigateToAgenda,
                onClientesClick = onNavigateToClients
            )
        },
        containerColor = FigmaBackground
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = FigmaGold)
                }
            }
            client == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cliente no encontrado",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
            else -> {
                ClientDetailContent(
                    client = client!!,
                    modifier = modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun ClientDetailContent(
    client: Client,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        DetailCard(title = "Información Personal") {
            DetailItem(label = "Nombre", value = client.name)
            DetailItem(label = "Apellido", value = client.lastName)
            DetailItem(label = "Tipo de Persona", value = client.personType.ifEmpty { "Física" })
            DetailItem(label = "Nacionalidad", value = client.nationality.ifEmpty { "No especificada" })
            DetailItem(label = "Fecha de Nacimiento", value = client.birthDate.ifEmpty { "No especificada" })
        }

        Spacer(modifier = Modifier.height(16.dp))

        DetailCard(title = "Información de Contacto") {
            DetailItem(label = "Email", value = client.email)
            DetailItem(label = "Teléfono", value = client.phone.ifEmpty { "No especificado" })
            DetailItem(label = "Dirección", value = client.address.ifEmpty { "No especificada" })
        }

        Spacer(modifier = Modifier.height(16.dp))

        DetailCard(title = "Descripción") {
            Text(
                text = client.description.ifEmpty { "Sin descripción" },
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun DetailCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(FigmaFieldBackground, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.boton_expedientes_expedientes),
                contentDescription = null,
                tint = FigmaGold,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = FigmaGold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ClientDetailScreenPreview() {
    LegallyTheme {
        ClientDetailScreen(clientId = "1")
    }
}

private val SampleClient = Client(
    id = "1",
    name = "Juan",
    lastName = "Pérez",
    nationality = "Mexicana",
    birthDate = "1990-01-01",
    phone = "555-1234",
    email = "juan.perez@example.com",
    personType = "Física",
    address = "Calle Falsa 123",
    description = "Cliente recurrente para casos civiles."
)

@Preview(showBackground = true)
@Composable
fun ClientDetailContentPreview() {
    LegallyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FigmaBackground
        ) {
            ClientDetailContent(client = SampleClient)
        }
    }
}
